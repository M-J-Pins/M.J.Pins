from datetime import timedelta, datetime
from enum import Enum
from typing import Union

from singleton_decorator import singleton
from sqlalchemy.ext.asyncio import AsyncSession
from starlette import status

from quick_wallet.database.models import User, VerificationCode
from quick_wallet.services.auth.verification.phone_verification_manager import PhoneVerificationManager
from quick_wallet.services.misc.jwt_manager import JWTManager
from quick_wallet.utils.datetime import check_datetime_expired


class AuthActionResult(Enum):
    CODE_SENT_SUCCESSFULLY = status.HTTP_202_ACCEPTED
    NOT_FOUND = status.HTTP_404_NOT_FOUND
    AUTH_OK = status.HTTP_200_OK
    COOLDOWN_NOT_EXPIRED = status.HTTP_423_LOCKED


async def check_cooldown_expired(db: AsyncSession, receiver: str) -> bool:
    last_code: VerificationCode = await VerificationCode.get(db, receiver=receiver)
    if last_code is None:
        return True
    return check_datetime_expired(last_code.date_created + timedelta(seconds=30))


@singleton
class AuthManager:

    @staticmethod
    async def phone_auth_request(db: AsyncSession, phone: str) -> AuthActionResult:
        if not await check_cooldown_expired(db, phone):
            return AuthActionResult.COOLDOWN_NOT_EXPIRED

        await PhoneVerificationManager().send_code(db, phone)
        return AuthActionResult.CODE_SENT_SUCCESSFULLY

    @staticmethod
    async def phone_auth(
        db: AsyncSession, code: str, phone: str
    ) -> (AuthActionResult, Union[str, None]):
        if not await PhoneVerificationManager().verify_code(db, code, phone):
            return AuthActionResult.NOT_FOUND, None

        db_user: User = await User.get(db, phone=phone)
        if db_user is None:
            db_user = await User.create(db, phone=phone)

        return AuthActionResult.AUTH_OK, await JWTManager().generate_jwt(db_user.id)
