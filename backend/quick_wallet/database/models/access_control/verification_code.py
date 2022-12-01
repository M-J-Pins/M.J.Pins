from sqlalchemy import Column
from sqlalchemy.dialects.postgresql import TEXT, TIMESTAMP

from quick_wallet.database.models.base import BaseTable


class VerificationCode(BaseTable):
    __tablename__ = "verification_code"

    code = Column(
        TEXT,
        nullable=False,
        doc="Verification code, that was sent to email/phone",
    )
    receiver = Column(TEXT, nullable=False, doc="Email or phone to verify")
    date_expire = Column(
        TIMESTAMP(timezone=True),
        nullable=False,
        doc="Date of expiration",
    )
