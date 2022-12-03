import requests
from singleton_decorator import singleton
from smsaero import SmsAero
from sqlalchemy.ext.asyncio import AsyncSession

from quick_wallet.config import get_settings
from quick_wallet.database.models import VerificationCode

from .verification_code_manager import VerificationCodeManager


@singleton
class PhoneVerificationManager(VerificationCodeManager):
    @classmethod
    async def send_code(cls, db: AsyncSession, phone: str) -> VerificationCode:
        settings = get_settings()
        db_code: VerificationCode = await super().send_code(db, phone)
        res = requests.get(
            f"https://"
            f"{settings.SMSAERO_EMAIL}:"
            f"{settings.SMSAERO_KEY}@gate.smsaero.ru/v2/flashcall/send?"
            f"phone={phone}&"
            f"code={db_code.code}"
        )
        return db_code
