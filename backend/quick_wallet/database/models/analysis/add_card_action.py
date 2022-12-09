from sqlalchemy import TEXT, Column, ForeignKey

from quick_wallet.database.models.base import BaseTable


class AddCardAction(BaseTable):
    __tablename__ = "add_card_action"

    image_name = Column(
        TEXT, nullable=False, unique=True, doc="Front side card image name in assets"
    )
    program_predict_shop_id = Column(
        ForeignKey("shop.id"),
        nullable=True,
        unique=False,
        doc="Shop similarity manager prediction",
    )
    program_predict_shop_name = Column(
        ForeignKey("shop.name"),
        nullable=True,
        unique=False,
        doc="Shop similarity manager prediction",
    )
    user_choice_shop_id = Column(
        ForeignKey("shop.id"),
        nullable=True,
        unique=False,
        doc="Shop id if card was added as STANDARD",
    )
    user_choice_shop_name = Column(
        TEXT,
        nullable=True,
        unique=False,
        doc="Shop name that was selected or entered by user",
    )
