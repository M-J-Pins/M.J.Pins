from uuid import UUID

from quick_wallet.schemas.base import AuthorizedRequest


class CardWalletRequest(AuthorizedRequest):
    wallet_id: UUID
    card_id: UUID
