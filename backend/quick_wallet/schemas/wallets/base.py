from __future__ import annotations

from uuid import UUID

from pydantic import BaseModel, constr, validator
from pydantic.fields import Field, List

from quick_wallet.schemas.base import AuthorizedRequest
from quick_wallet.schemas.cards import CardResponse


class WalletId(AuthorizedRequest):
    wallet_id: UUID


class WalletCardScheme(CardResponse):
    editable: bool
    owner: constr(
        strip_whitespace=True,
        regex=r"^(\+)[1-9][0-9\-\(\)\.]{9,15}$",
    )

    @validator("owner")
    def validate_phone(cls, phone):
        new_phone = [p if p.isdigit() else "" for p in phone]
        return "".join(new_phone)


class WalletResponse(BaseModel):
    editable: bool
    cards: List[WalletCardScheme]
    user_phones: List[
        constr(
            strip_whitespace=True,
            regex=r"^(\+)[1-9][0-9\-\(\)\.]{9,15}$",
        )
    ]

    @validator("user_phones")
    def validate_phone(cls, phones):
        new_phones = []
        for phone in phones:
            new_phones.append("".join([p if p.isdigit() else "" for p in phone]))
        return new_phones


class WalletShortInfo(BaseModel):
    id: UUID
    name: str = Field(..., max_length=25)
    color: str = Field(..., min_length=6, max_length=6)
    editable: bool


class WalletListResponse(BaseModel):
    wallets: List[WalletShortInfo]
