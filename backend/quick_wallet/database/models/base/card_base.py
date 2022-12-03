from sqlalchemy import Column
from sqlalchemy.dialects.postgresql import TEXT, TIMESTAMP, UUID
from sqlalchemy.sql import func

from .base import BaseTable


class CardBase(BaseTable):
    __abstract__ = True

    barcode_data = Column(
        TEXT,
        nullable=False,
        doc="Card barcode data",
    )
    note = Column(
        TEXT,
        nullable=False,
        doc="Special data which is created by the owner",
    )
