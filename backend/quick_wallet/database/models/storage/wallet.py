import enum

from sqlalchemy import Column, Enum, ForeignKey
from sqlalchemy.dialects.postgresql import TEXT

from quick_wallet.database.models.base import BaseTable


class WalletColor(enum.Enum):
    COLOR1 = "4c9141"


class Wallet(BaseTable):
    __tablename__ = "wallet"

    name = Column(TEXT, nullable=False, unique=True, doc="Name of the wallet")
    owner_id = Column(
        ForeignKey("user.id"),
        nullable=False,
        unique=False,
        onupdate="CASCADE",
        doc="Wallet owner id from user table",
    )
    color = Column(
        Enum(WalletColor),
        default=WalletColor.COLOR1,
        nullable=True,
        unique=False,
        doc="Shop type to filter cards",
    )
