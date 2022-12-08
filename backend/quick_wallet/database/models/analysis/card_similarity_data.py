from sqlalchemy import TEXT, Column, ForeignKey

from quick_wallet.database.models.base import BaseTable


class CardSimilarityData(BaseTable):
    __tablename__ = "card_similarity_data"

    shop_id = Column(
        ForeignKey("shop.id"),
        nullable=False,
        unique=False,
        doc="Shop ID from shop table",
    )
    text = Column(TEXT, nullable=False, unique=False, doc="Text to count similarity")
    color = Column(
        TEXT, nullable=False, unique=False, doc="Card color to count similarity"
    )
