from pydantic import Field

from quick_wallet.schemas.base import AuthorizedRequest


class QuickCardsRequest(AuthorizedRequest):
    longitude: float = Field(..., ge=-180, le=180)
    latitude: float = Field(..., ge=-90, le=90)
