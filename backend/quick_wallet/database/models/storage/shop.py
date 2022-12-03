import enum

from sqlalchemy import Column, Enum
from sqlalchemy.dialects.postgresql import TEXT, UUID

from quick_wallet.database.models.base import BaseTable


class ShopCategoryEnum(enum.Enum):
    FOOD = "FOOD"
    CLOTHES = "CLOTHES"
    SPORT = "SPORT"
    ANY = "ANY"


class Shop(BaseTable):
    __tablename__ = "shop"

    name = Column(TEXT, nullable=False, unique=True, doc="Name of the shop")
    map_search_string = Column(
        TEXT,
        nullable=False,
        unique=False,
        doc="String for map api to find the nearest shop",
    )
    icon_url = Column(TEXT, nullable=False, unique=False, doc="Url to load icon image")
    category = Column(
        Enum(ShopCategoryEnum),
        nullable=True,
        unique=False,
        doc="Shop type to filter cards",
    )
    card_image_url = Column(
        TEXT, nullable=True, unique=False, doc="URL to load card image"
    )
