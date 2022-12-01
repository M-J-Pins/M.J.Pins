from sqlalchemy import Column, select
from sqlalchemy.dialects.postgresql import TEXT

from quick_wallet.database.models.base import BaseTable


class User(BaseTable):
    __tablename__ = "user"

    phone = Column(TEXT, nullable=False, unique=True, doc="User phone number")
