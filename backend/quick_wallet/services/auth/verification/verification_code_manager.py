import random
from datetime import datetime, timedelta

from sqlalchemy.ext.asyncio import AsyncSession

from quick_wallet.database.models import VerificationCode
from quick_wallet.utils.datetime import (
    calculate_future_datetime,
    check_datetime_expired,
)


class VerificationCodeManager:
    @classmethod
    def generate_code(cls) -> str:
        double: int = random.randint(0, 9)
        code: str = f"{double}{random.randint(0, 9)}{double}{random.randint(0, 9)}"
        return code

    @classmethod
    async def send_code(cls, db: AsyncSession, receiver: str) -> VerificationCode:
        """Send code message to phone or email and remove previous codes"""
        await VerificationCode().delete(db, receiver=receiver)
        code: str = cls.generate_code()
        return await cls.save_code(db, code, receiver)

    @classmethod
    async def save_code(
        cls,
        db: AsyncSession,
        code: str,
        receiver: str,
        life_time: timedelta = timedelta(minutes=15),
    ) -> VerificationCode:
        """Save code to db"""
        date_expire: datetime = calculate_future_datetime(life_time)
        new_model = await VerificationCode.create(
            db, code=code, receiver=receiver, date_expire=date_expire
        )
        return new_model

    @staticmethod
    async def verify_code(db: AsyncSession, code, receiver) -> bool:
        db_code: VerificationCode = await VerificationCode().get(
            db, code=code, receiver=receiver
        )
        if db_code is None:
            print("!!!\n!!!\n!!!\n")
            return False

        if check_datetime_expired(db_code.date_expire):
            await VerificationCode().delete(db, receiver=receiver)
            return False
        await VerificationCode().delete(db, receiver=receiver)
        return True
