from __future__ import annotations

from uuid import UUID

from pydantic import BaseModel, constr
from pydantic.fields import Field, List

from quick_wallet.schemas.cards import CardResponse


class WalletCardScheme(CardResponse):
    editable: bool
    owner: constr(
        strip_whitespace=True,
        regex=r"^(\+)[1-9][0-9\-\(\)\.]{9,15}$",
    )


class WalletResponse(BaseModel):
    editable: bool
    cards: List[WalletCardScheme]
    user_phones: List[
        constr(
            strip_whitespace=True,
            regex=r"^(\+)[1-9][0-9\-\(\)\.]{9,15}$",
        )
    ]


class WalletShortInfo(BaseModel):
    id: UUID
    name: str = Field(..., max_length=25)
    color: str = Field(..., min_length=6, max_length=6)
    editable: bool


class WalletListResponse(BaseModel):
    wallets: List[WalletShortInfo]
