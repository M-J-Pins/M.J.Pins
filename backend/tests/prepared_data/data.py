from quick_wallet.database.models.storage import ShopCategoryEnum
from quick_wallet.database.models.storage import CardColorEnum

shops = [
    {
          "id": "6a6e8f44-3d78-4af8-bad5-704180a28617",
          "name": "Магнит",
          "icon_url": "https://i.ibb.co/R04kHDf/image.png",
          "category": ShopCategoryEnum.ANY,
          "card_image_url": "https://i.ibb.co/LRktZCv/image.jpg",
          "map_search_string": "Магнит,Магнит Косметик,Магнит Семейный,Магнит Аптека,Магнит Опт,Магнит Экстра,Магнит Мастер",
    },
    {
        "id": "7a16a654-3f1a-4d0f-88ee-2af5ec424441",
        "name": "Пятёрочка",
        "icon_url": "https://i.ibb.co/L8zpNhj/image.png",
        "category": ShopCategoryEnum.FOOD,
        "card_image_url": "https://i.ibb.co/d0p2Wgz/image.jpg",
        "map_search_string": "Пятёрочка",
    },
    {
        "id": "50a7cc81-2ed4-446b-8627-0c8b3ef6486e",
        "name": "Metro",
        "icon_url": "https://i.ibb.co/jTnW1fy/image.png",
        "category": ShopCategoryEnum.FOOD,
        "card_image_url": "https://i.ibb.co/n1Lt4Zb/image.jpg",
        "map_search_string": "Metro Cash&Carry",
    },
    {
        "id": "b9bd21c6-1800-45d8-96ad-ce9ca08e4d59",
        "name": "Спортмастер",
        "icon_url": "https://i.ibb.co/WH6MrPX/image.png",
        "category": ShopCategoryEnum.SPORT,
        "card_image_url": "https://i.ibb.co/k0c30Hs/image.png",
        "map_search_string": "Спортмастер,Спортмастер Pro",
    },
    {
        "id": "6dc09c82-95ad-4f70-ac5e-aa613086573f",
        "name": "Читай-город",
        "icon_url": "https://i.ibb.co/KVJJkxj/image.png",
        "category": ShopCategoryEnum.OFFICE,
        "card_image_url": "https://i.ibb.co/zPfrRB8/image.png",
        "map_search_string": "Читай-город",
    },
    {
        "id": "89c298fb-2667-42bd-9f61-9a43ac869d5f",
        "name": "Буквоед",
        "icon_url": "https://i.ibb.co/9GMsXWd/image.png",
        "category": ShopCategoryEnum.OFFICE,
        "card_image_url": "https://i.ibb.co/TW1BYqW/image.png",
        "map_search_string": "Буквоед",
    },
    {
        "id": "8aebb9f6-1d92-427f-947c-388901c985ab",
        "name": "Триал-Спорт",
        "icon_url": "https://i.ibb.co/4RtvzQ2/image.jpg",
        "category": ShopCategoryEnum.SPORT,
        "card_image_url": "https://i.ibb.co/KG3WrkS/image.jpg",
        "map_search_string": "Триал-Спорт",
    },
    {
        "id": "0aed88e0-6d28-46b9-9037-5319fa0b905b",
        "name": "O'STIN",
        "icon_url": "https://i.ibb.co/1Zg270N/image.png",
        "category": ShopCategoryEnum.CLOTHES,
        "card_image_url": "https://i.ibb.co/CWKNgHW/image.png",
        "map_search_string": "O'STIN,Остин",
    },
    {
        "id": "92f371d8-e912-406f-9f76-763e1973bb5f",
        "name": "Ситилинк",
        "icon_url": "https://i.ibb.co/CBhJ14k/image.jpg",
        "category": ShopCategoryEnum.ELECTRONICS,
        "card_image_url": "https://i.ibb.co/DKFYdcc/image.jpg",
        "map_search_string": "Ситилинк,Ситилинк мини пункт выдачи",
    }
]

users = [
    {
        "id": "8c06da29-6198-4fbc-823a-fa3e47b94c64",
        "phone": "70000000000",
    },
    {
        "id": "065381f8-953c-46ce-98f4-0e6625024160",
        "phone": "70000000001",
    },
    {
        "id": "3a9b910b-207a-440f-8a79-ede85a0f5884",
        "phone": "70000000002",
    },
    {
        "id": "0d00da6c-7d58-40d8-a8d1-215d11b1f196",
        "phone": "70000000003",
    },
    {
        "id": "918aceb7-034d-478e-a798-0f28d47dc222",
        "phone": "70000000004",
    },
]

cards = [
    # 1 user
    {
        "id": "0bca364e-efd0-4f37-aef9-ec401ddf7b51",
        "type": "STANDARD",
        "owner_id": "8c06da29-6198-4fbc-823a-fa3e47b94c64",
        "barcode_data": "0000",
        "shop_id": "6a6e8f44-3d78-4af8-bad5-704180a28617",
        "image_url": "https://i.ibb.co/LRktZCv/image.jpg",
        "shop_name": "Магнит",
        "color": None,
        "map_search_string": "Магнит,Магнит Косметик,Магнит Семейный,Магнит Аптека,Магнит Опт,Магнит Экстра,Магнит Мастер",
        "category": ShopCategoryEnum.ANY,
    },
    {
        "id": "3acf7853-1299-40df-a2fe-544304dd47e6",
        "type": "STANDARD",
        "owner_id": "8c06da29-6198-4fbc-823a-fa3e47b94c64",
        "barcode_data": "0001",
        "shop_id": "7a16a654-3f1a-4d0f-88ee-2af5ec424441",
        "image_url": "https://i.ibb.co/d0p2Wgz/image.jpg",
        "shop_name": "Пятёрочка",
        "color": None,
        "map_search_string": "Пятёрочка",
        "category": ShopCategoryEnum.FOOD,
    },
    {
        "id": "4e0abd75-c7c2-4252-aac8-aa967eed64a8",
        "type": "STANDARD",
        "owner_id": "8c06da29-6198-4fbc-823a-fa3e47b94c64",
        "barcode_data": "0009",
        "shop_id": "92f371d8-e912-406f-9f76-763e1973bb5f",
        "image_url": "https://i.ibb.co/DKFYdcc/image.jpg",
        "shop_name": "Ситилинк",
        "color": None,
        "map_search_string": "Ситилинк,Ситилинк мини пункт выдачи",
        "category": ShopCategoryEnum.ELECTRONICS,
    },
    {
        "id": "0062e29c-00c9-4ff1-b2e5-9b437b1bfeba",
        "type": "UNKNOWN",
        "owner_id": "8c06da29-6198-4fbc-823a-fa3e47b94c64",
        "barcode_data": "0002",
        "shop_id": None,
        "image_url": None,
        "shop_name": "МВидео",
        "color": CardColorEnum.PINK,
        "map_search_string": None,
        "category": ShopCategoryEnum.ELECTRONICS,
    },
    {
        "id": "9eaada03-81d7-4ecc-b4d4-b99c163a9f7e",
        "type": "UNKNOWN",
        "owner_id": "8c06da29-6198-4fbc-823a-fa3e47b94c64",
        "barcode_data": "0003",
        "shop_id": None,
        "image_url": None,
        "shop_name": "Ярче",
        "color": CardColorEnum.TURQUOISE,
        "map_search_string": None,
        "category": ShopCategoryEnum.FOOD,
    },
    # 2 user
    {
        "id": "6a48c56c-84a5-458b-89e5-7382a496a020",
        "type": "STANDARD",
        "owner_id": "065381f8-953c-46ce-98f4-0e6625024160",
        "barcode_data": "0004",
        "shop_id": "6a6e8f44-3d78-4af8-bad5-704180a28617",
        "image_url": "https://i.ibb.co/LRktZCv/image.jpg",
        "shop_name": "Магнит",
        "color": None,
        "map_search_string": "Магнит,Магнит Косметик,Магнит Семейный,Магнит Аптека,Магнит Опт,Магнит Экстра,Магнит Мастер",
        "category": ShopCategoryEnum.ANY,
    },
    {
        "id": "38861809-7fb2-4ddb-9db5-6fc8cae8a4c5",
        "type": "STANDARD",
        "owner_id": "065381f8-953c-46ce-98f4-0e6625024160",
        "barcode_data": "0005",
        "shop_id": "b9bd21c6-1800-45d8-96ad-ce9ca08e4d59",
        "image_url": "https://i.ibb.co/k0c30Hs/image.png",
        "shop_name": "Спортмастер",
        "color": None,
        "map_search_string": "Спортмастер,Спортмастер Pro",
        "category": ShopCategoryEnum.SPORT,
    },
    {
        "id": "58c31f99-19e3-455c-911b-169859b6cab6",
        "type": "UNKNOWN",
        "owner_id": "065381f8-953c-46ce-98f4-0e6625024160",
        "barcode_data": "0006",
        "shop_id": None,
        "image_url": None,
        "shop_name": "Ярче",
        "color": CardColorEnum.PINK,
        "map_search_string": None,
        "category": ShopCategoryEnum.FOOD,
    },
    # 3 user
    {
        "id": "985be2ce-abd8-4c2a-b0a4-11de0e79c1ee",
        "type": "STANDARD",
        "owner_id": "3a9b910b-207a-440f-8a79-ede85a0f5884",
        "barcode_data": "0007",
        "shop_id": "6dc09c82-95ad-4f70-ac5e-aa613086573f",
        "image_url": "https://i.ibb.co/zPfrRB8/image.png",
        "shop_name": "Читай-город",
        "color": None,
        "map_search_string": "Читай-город",
        "category": ShopCategoryEnum.OFFICE,
    }
]

# Metro
valid_standard_card = {
    "barcode_data": "1000",
    "shop_id": "50a7cc81-2ed4-446b-8627-0c8b3ef6486e",
}

# МВидео
valid_unknown_card = {
    "barcode_data": "1001",
    "shop_name": "Еда",
    "category": "Продукты",
}

