from sqlalchemy import TEXT, Column, ForeignKey

from quick_wallet.database.models.base import BaseTable


class ShopCardText(BaseTable):
    __tablename__ = "shop_card_text"

    shop_id = Column(
        ForeignKey("shop.id"),
        nullable=False,
        unique=False,
        doc="Shop ID from shop table",
    )
    text = Column(TEXT, nullable=False, unique=False, doc="Text to count similarity")
