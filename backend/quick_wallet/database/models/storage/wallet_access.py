import enum

from sqlalchemy import Column, Enum, ForeignKey
from sqlalchemy.dialects.postgresql import TEXT

from quick_wallet.database.models.base import BaseTable


class WalletAccess(BaseTable):
    __tablename__ = "wallet_access"

    wallet_id = Column(
        ForeignKey("wallet.id"),
        nullable=False,
        unique=False,
        onupdate="CASCADE",
        doc="Wallet id",
    )
    user_id = Column(
        ForeignKey("user.id"),
        nullable=False,
        unique=False,
        onupdate="CASCADE",
        doc="User's id who has access to edit wallet",
    )
