### Запуск приложения

```bash
make db
make revision
make migrate

poetry install
make run
```

Документация ручек будет доступна по адресу http://localhost:8000/swagger

###### Директории

ORM модели таблиц БД, инструменты подключения, миграции</br>
```quick_wallet/database```

Роуты API</br>
```quick_wallet/endpoints```

Схемы запросов/ответов API</br>
```quick_wallet/schemas```

Класс взаимодействия с API яндекс карт</br>
```quick_wallet/services/map_searcher```

Класс определения магазина по фото передней части карты</br>
!! ATTENTION !! будет добавлен еще один метод определния, основанный на сравнении картинок из датасета каджого магазина </br>
```quick_wallet/services/misc/shop_similarity_manager.py```