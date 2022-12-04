from sqlalchemy import TEXT, Column, ForeignKey

from quick_wallet.database.models.base import BaseTable


class ShopCardColor(BaseTable):
    __tablename__ = "shop_card_color"

    shop_id = Column(
        ForeignKey("shop.id"),
        nullable=False,
        unique=False,
        doc="Shop ID from shop table",
    )
    color = Column(
        TEXT, nullable=False, unique=False, doc="Card color to count similarity"
    )
