from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.ext.asyncio import AsyncSession
from starlette import status

from quick_wallet.database.connection import get_session
from quick_wallet.schemas.base import AuthorizedRequest
from quick_wallet.schemas.wallets import WalletListResponse
from quick_wallet.services.misc import ConvertManager, JWTManager

api_router = APIRouter(prefix="/wallets")


@api_router.get(
    "/my",
    response_model=WalletListResponse,
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
async def my_wallets(
    request: AuthorizedRequest = Depends(),
    db: AsyncSession = Depends(get_session),
):
    """
    Ручка возвращает список *коротких* объектов кошельков, доступных пользователю
    """
    user_id = JWTManager().id_from_jwt(request.token)
    if user_id is None:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED)

    return await ConvertManager.user_id2wallet_list_scheme(db, user_id)
