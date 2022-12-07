from enum import Enum
from typing import List
from uuid import UUID

from sqlalchemy.ext.asyncio import AsyncSession
from starlette import status

from quick_wallet.database.models import Card, User
from quick_wallet.database.models.storage import Wallet, WalletAccess, WalletCard


class WalletActionResult(Enum):
    SUCCESS: status.HTTP_200_OK
    NO_CARD: status.HTTP_404_NOT_FOUND
    NO_WALLET: status.HTTP_404_NOT_FOUND
    NO_ACCESS: status.HTTP_403_FORBIDDEN
    NO_USER: status.HTTP_404_NOT_FOUND
    FORBIDDEN: status.HTTP_403_FORBIDDEN


class WalletManager:
    @staticmethod
    async def create_wallet(
        db: AsyncSession,
        owner_id: UUID,
        name: str,
        cards: List[UUID],
        user_ids: List[UUID],
    ) -> (WalletActionResult, Wallet):
        new_wallet = await Wallet.create(db, owner_id=owner_id, name=name)
        res = WalletManager.give_access(db, owner_id, new_wallet.id, owner_id)
        for card_id in cards:
            res, wallet_card = await WalletManager.add_card(
                db, owner_id, new_wallet.id, card_id
            )

        for user_id in user_ids:
            res = WalletManager.give_access(db, owner_id, new_wallet.id, user_id)

        return WalletActionResult.SUCCESS, new_wallet

    @staticmethod
    async def give_access(
        db: AsyncSession, wallet_owner_id, wallet_id: UUID, user_id: UUID
    ) -> (WalletActionResult, WalletAccess):
        if await Wallet.get(db, id=wallet_id) is None:
            return WalletActionResult.NO_WALLET, None

        if wallet_owner_id != (await Wallet.get(db, id=wallet_id)).owner_id:
            return WalletActionResult.NO_ACCESS, None

        if await User.get(db, id=user_id) is None:
            return WalletActionResult.NO_USER, None

        wallet_access = await WalletAccess.create(
            db, wallet_id=wallet_id, user_id=user_id
        )
        return WalletActionResult.SUCCESS, wallet_access

    @staticmethod
    async def add_card(
        db: AsyncSession, card_owner_id: UUID, wallet_id: UUID, card_id: UUID
    ) -> (WalletActionResult, WalletCard):
        db_card: Card = await Card.get(db, id=card_id)
        if db_card is None:
            return WalletActionResult.NO_CARD, None

        if await Wallet.get(db, id=wallet_id) is None:
            return WalletActionResult.NO_WALLET, None

        if await WalletAccess.get(db, user_id=card_owner_id) is None:
            return WalletActionResult.NO_ACCESS, None

        if card_owner_id != db_card.owner_id:
            return WalletActionResult.NO_ACCESS, None

        wallet_card = await WalletCard.create(
            db, wallet_id=wallet_id, card_id=card_id, card_owner_id=db_card.owner_id
        )
        return WalletActionResult.SUCCESS, wallet_card

    @staticmethod
    async def delete_card(
        db: AsyncSession, card_owner_id, wallet_id: UUID, card_id: UUID
    ) -> WalletActionResult:
        if await Wallet.get(db, id=wallet_id) is None:
            return WalletActionResult.NO_WALLET

        if await WalletCard.get(db, card_id=card_id, wallet_id=wallet_id) is None:
            return WalletActionResult.NO_CARD

        if (
            await WalletCard.get(db, card_id=card_id, card_owner_id=card_owner_id)
            is None
        ):
            return WalletActionResult.NO_ACCESS
        await WalletCard.delete(db, wallet_id=wallet_id, card_id=card_id)

    @staticmethod
    async def revoke_access(
        db: AsyncSession, wallet_owner_id: UUID, wallet_id: UUID, user_id: UUID
    ) -> WalletActionResult:
        db_wallet: Wallet = await Wallet.get(db, id=wallet_id)
        if db_wallet is None:
            return WalletActionResult.NO_WALLET

        if wallet_owner_id != db_wallet.owner_id:
            return WalletActionResult.NO_ACCESS

        if user_id == db_wallet.owner_id:
            return WalletActionResult.FORBIDDEN

        await WalletCard.delete(db, wallet_id=wallet_id, card_owner_id=user_id)
        await WalletAccess.delete(db, wallet_id=wallet_id, user_id=user_id)
        return WalletActionResult.SUCCESS

    @staticmethod
    async def leave_wallet(
        db: AsyncSession, user_id: UUID, wallet_id: UUID
    ) -> WalletActionResult:
        db_wallet: Wallet = await Wallet.get(db, id=wallet_id)
        if db_wallet is None:
            return WalletActionResult.NO_WALLET

        if await WalletAccess.get(db, user_id=user_id, wallet_id=wallet_id) is None:
            return WalletActionResult.NO_ACCESS

        if user_id == db_wallet.owner_id:
            return WalletActionResult.FORBIDDEN

        await WalletCard.delete(db, wallet_id=wallet_id, card_owner_id=user_id)
        await WalletAccess.delete(db, wallet_id=wallet_id, user_id=user_id)
        return WalletActionResult.SUCCESS

    @staticmethod
    async def delete_wallet(
        db: AsyncSession, wallet_owner_id: UUID, wallet_id: UUID
    ) -> WalletActionResult:
        db_wallet: Wallet = await Wallet.get(db, id=wallet_id)
        if db_wallet is None:
            return WalletActionResult.NO_WALLET

        if wallet_owner_id != db_wallet.owner_id:
            return WalletActionResult.NO_ACCESS

        await WalletCard.delete(db, wallet_id=wallet_id)
        await WalletAccess.delete(db, wallet_id=wallet_id)
        await Wallet.delete(db, id=wallet_id)
