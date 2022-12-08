from typing import List

from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.ext.asyncio import AsyncSession
from starlette import status

from quick_wallet.config import get_settings
from quick_wallet.database.connection import get_session
from quick_wallet.database.models import Card, CardTypeEnum
from quick_wallet.schemas.cards import CardListResponse, CardResponse, QuickCardsRequest
from quick_wallet.services.map_seacher import MapSearchManager, Point, PointParser
from quick_wallet.services.misc import JWTManager


class CardDist:
    def __init__(self, card: Card, dist: float):
        self.card = card
        self.dist = dist

    def __lt__(self, other):
        return self.dist < other.dist


api_router = APIRouter(prefix="/cards")


@api_router.get(
    "/quick",
    response_model=CardListResponse,
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
async def get_quick_cards(
    request: QuickCardsRequest = Depends(),
    db: AsyncSession = Depends(get_session),
):
    """
    Ручка принимает получает токен пользователя и его координаты
    Отдает список не более чем из 5 карт ближайших магазинов, отсортированных в порядке близости
    """

    user_id = JWTManager().id_from_jwt(request.token)
    if user_id is None:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED)

    position: Point = Point(request.longitude, request.latitude)
    card_dist_list: List[CardDist] = []

    db_standard_cards: List[Card] = await Card.get_all(db, owner_id=user_id, type=CardTypeEnum.STANDARD)
    for card in db_standard_cards:
        cur_dist: float = MapSearchManager.distance_to_nearest_shop(
            position, card.map_search_string, get_settings().SEARCH_RADIUS
        )
        card_dist_list.append(CardDist(card, cur_dist))

    db_unknown_cards: List[Card] = await Card.get_all(db, owner_id=user_id, type=CardTypeEnum.UNKNOWN)
    for card in db_unknown_cards:
        cur_dist: float = MapSearchManager.distance_to_nearest_shop(
            position, card.shop_name, get_settings().SEARCH_RADIUS
        )
        card_dist_list.append(CardDist(card, cur_dist))

    card_dist_list.sort()
    card_list: List[CardResponse] = [
        CardResponse.from_orm(card_dist.card) for card_dist in card_dist_list[-5::]
    ]
    response: CardListResponse = CardListResponse(cards=card_list)

    return response
