import enum

from sqlalchemy import Column, Enum, ForeignKey
from sqlalchemy.dialects.postgresql import TEXT, UUID

from quick_wallet.database.models.base import BaseTable

from .shop   import ShopCategoryEnum


class CardTypeEnum(enum.Enum):
    STANDART = "STANDART"
    UNKNOWN = "UNKNOWN"


class CardColorEnum(enum.Enum):
    PINK = "ffc0cb"
    TURQUOISE = "30d5c8"


class Card(BaseTable):
    __tablename__ = "card"

    type = Column(Enum(CardTypeEnum), nullable=False, doc="Card type")
    owner_id = Column(
        UUID,
        ForeignKey("user.id", onupdate="CASCADE", ondelete="CASCADE"),
        nullable=False,
        doc="Card owner ID from User table",
    )
    barcode_data = Column(
        TEXT,
        nullable=False,
        doc="Card barcode data",
    )
    note = Column(
        TEXT,
        nullable=True,
        doc="Special data which is created by the owner",
    )
    shop_id = Column(
        UUID, nullable=True, unique=False, doc="Shop ID from shop table"
    )
    image_url = Column(TEXT, nullable=True, unique=False, doc="URL to load card image (if type=STANDART")
    shop_name = Column(
        TEXT,
        nullable=False,
        unique=False,
        doc="Name of the shop which was written by user or from shop table (if type=STANDART)",
    )
    color = Column(
        Enum(CardColorEnum), nullable=True, unique=False, doc="Card color in mobile interface (if type=UNKNOWN)"
    )
    map_search_string = Column(
        TEXT,
        nullable=True,
        unique=False,
        doc="String for map api to find the nearest shop",
    )
    category = Column(Enum(ShopCategoryEnum), nullable=False, doc="Card type")
