from typing import Union, List
from uuid import UUID

from sqlalchemy.ext.asyncio import AsyncSession

from quick_wallet.database.models import Card
from quick_wallet.database.models.account import User
from quick_wallet.database.models.storage import Wallet, WalletCard, WalletAccess
from quick_wallet.schemas.wallets import WalletResponse, WalletCardScheme, WalletListResponse, WalletShortInfo
from quick_wallet.schemas.cards import CardResponse


class ConvertManager:

    @staticmethod
    async def phone2user_id(db: AsyncSession, phone: str) -> Union[UUID, None]:
        db_user: User = await User.get(db, phone=phone)
        if db_user is None:
            return None
        return db_user.id

    @staticmethod
    async def user_id2phone(db: AsyncSession, user_id: UUID) -> Union[str, None]:
        db_user: User = await User.get(db, id=user_id)
        if db_user is None:
            return None
        return db_user.phone

    @staticmethod
    async def wallet_id2wallet_scheme(db: AsyncSession, user_id: UUID, wallet_id: UUID) -> WalletResponse:
        db_wallet: Wallet = await Wallet.get(db, id=wallet_id)

        user_ids: List[UUID] = [user_id for user_id in await WalletAccess.get_all(db, wallet_id=wallet_id)]
        user_phones: List[str] = [f"+{(await User.get(db, id=user_id)).phone}" for user_id in user_ids]

        db_card_ids: List[UUID] = [card.card_id for card in await WalletCard.get_all(db, wallet_id=wallet_id)]
        wallet_cards: List[WalletCardScheme] = []
        for card_id in db_card_ids:
            card: Card = await Card.get(db, id=card_id)
            wallet_cards.append(
                WalletCardScheme(
                    **CardResponse.from_orm(card).__dict__,
                    editable=(True if card.id == user_id else False),
                    owner=f"+{ConvertManager.user_id2phone(db, card.owner_id)}"
                )
            )

        return WalletResponse(
            editable=(True if user_id == db_wallet.owner_id else False),
            cards=wallet_cards,
            user_phones=user_phones
        )

    @staticmethod
    async def user_id2wallet_list_scheme(db: AsyncSession, user_id: UUID) -> WalletListResponse:
        wallet_ids: List[UUID] = [wallet_access.id for wallet_access in await WalletAccess.get_all(db, user_id=user_id)]
        short_wallets: List[WalletShortInfo] = []
        for wallet_id in wallet_ids:
            db_wallet: Wallet = await Wallet.get(db, id=wallet_id)
            short_wallets.append(
                WalletShortInfo(
                    id=db_wallet.id,
                    name=db_wallet.name,
                    color=db_wallet.color,
                    editable=(True if user_id == db_wallet.owner_id else False)
                )
            )

        return WalletListResponse(wallets=short_wallets)
