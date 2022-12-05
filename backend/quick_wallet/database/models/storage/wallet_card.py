import enum

from sqlalchemy import Column, Enum, ForeignKey
from sqlalchemy.dialects.postgresql import TEXT

from quick_wallet.database.models.base import BaseTable


class WalletCard(BaseTable):
    __tablename__ = "wallet_card"

    card_id = Column(
        ForeignKey("card.id"),
        nullable=False,
        unique=False,
        onupdate="CASCADE",
        doc="Card id",
    )
    wallet_id = Column(
        ForeignKey("wallet.id"),
        nullable=False,
        unique=False,
        onupdate="CASCADE",
        doc="Wallet id",
    )
    card_owner_id = Column(
        ForeignKey("user.id"),
        nullable=False,
        unique=False,
        onupdate="CASCADE",
        doc="User id who had been added this card",
    )
