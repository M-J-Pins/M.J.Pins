from typing import List
from uuid import UUID

from fastapi import APIRouter, Body, Depends, File, HTTPException, UploadFile
from sqlalchemy.ext.asyncio import AsyncSession
from starlette import status

from quick_wallet.database.connection import get_session
from quick_wallet.schemas.wallets import CreateWalletRequest, WalletResponse
from quick_wallet.services.misc import JWTManager, ConvertManager
from quick_wallet.services.wallets import WalletManager, WalletActionResult
from quick_wallet.database.models.storage import Wallet

api_router = APIRouter(prefix="/wallets")


@api_router.post(
    "/create",
    response_model=WalletResponse,
    status_code=status.HTTP_200_OK,
    responses={
        status.HTTP_422_UNPROCESSABLE_ENTITY: {
            "description": "Invalid data (validation error)",
        },
        status.HTTP_401_UNAUTHORIZED: {
            "description": "Invalid or unknown token",
        },
        status.HTTP_500_INTERNAL_SERVER_ERROR: {
            "description": "Oops.. Server was broken by your request",
        },
    },
)
async def add_shop(
    request: CreateWalletRequest = Body(
        ...,
        example={
            "token": "<UUID token>",
            "name": "Семья",
            "cards": [
                "<UUID card 1>",
                "<UUID card 2>",
            ],
            "users": [
                "+79507990996",
            ]
        }
    ),
    db: AsyncSession = Depends(get_session),
):
    """
    Ручка принимает имя нового кошелька,
    список пользователей, которым он будет доступен,
    список карт, которые надо добавить при создании
    Возвращает объект нового кошелька
    """
    user_id = JWTManager().id_from_jwt(request.token)
    if user_id is None:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED)

    user_ids: List[UUID] = []
    for phone in request.users:
        id = await ConvertManager.phone2user_id(db, phone)
        if id is not None:
            user_ids.append(id)

    res, wallet = await WalletManager.create_wallet(db, user_id, request.name, request.cards, user_ids)
    if res != WalletActionResult.SUCCESS:
        raise HTTPException(status_code=res.value)


