/*
 * Filename	: language.js
 * Function	:
 * Comment 	:
				공통 	BO0001 ~ BO0999
				메뉴	BO1001 ~ BO1999
				데이터테이블 타이틀/상세	BO2001 ~ BO2999
				버튼/카렌더 이벤트 정의	BO3001 ~ BO3999
				이벤트/경고 메시지	BO4001 ~ BO4999

 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

define({
		/*common*/
		 'BO0001':"BAK Админ"
		,'BO0002':"Логин"
		,'BO0003':"ID"
		,'BO0004':"Пароль"
		,'BO0005':"сохранить ID"
		,'BO0006':"По вашему запросу ничего не найдено."
		,'BO0007':"Проверьте поисковый запрос."
		,'BO0008':"Обязательно для заполнения."
		,'BO0009':"Пожалуйста заполните обязательные поля."
		,'BO0010':"Нет данных."
		,'BO0011':"Поиск"
		,'BO0012':"Закладки"
		,'BO0013':"Список"
		,'BO0014':"Сохранить"
		,'BO0015':"Изменить"
		,'BO0016':"Удалить"
		,'BO0017':"Добавить"
		,'BO0018':"Короткий период"
		,'BO0019':"Предыдущий"
		,'BO0020':"Следующий"
		,'BO0021':"Завершено"
		,'BO0022':"Уточнения"
		,'BO0023':"Подтверждение"
		,'BO0024':"Выбор"
		,'BO0025':"Создать"
		,'BO0026':"Отмена"
		,'BO0027':"Дата создания"
		,'BO0028':"Дата обновления"
		,'BO0029':"Этикетка"
		,'BO0030':"₽"
		/* menu */
		,'BO1001':"Домой"
		,'BO1002':"Панель управлени"
		,'BO1003':"Продажи"
		,'BO1004':"Итог по позициям"
		,'BO1005':"Итог по продажам"
		,'BO1006':"Продажи товаров"
		,'BO1007':"Категории продаж"
		,'BO1008':"Продажи по категориям"
		,'BO1009':"Продажи по типу оплаты"
		,'BO1010':"Продажи по времени"
		,'BO1011':"Продажи по неделям"
		,'BO1012':"Информация по продажам"
		,'BO1013':"Карта подтверждена"
		,'BO1014':"Категория"
		,'BO1015':"Позиции"
		,'BO1016':"POS-PLU категории"
		,'BO1017':"POS-PLU настройки"
		,'BO1018':"Tab-DP категории"
		,'BO1019':"Tab-DP настройки"
		,'BO1020':"Moblie-DP категории"
		,'BO1021':"Moblie-DP настройки"
		,'BO1022':"POS-Table настройки"
		,'BO1023':"POS-Memo настройки"
		,'BO1024':"Награда"
		,'BO1025':"Информация по наградам"
		,'BO1026':"Информация по печатям"
		,'BO1027':"Подобнее о печати"
		,'BO1028':"Информация по купонам"
		,'BO1029':"Подоробнее о купоне"
		,'BO1030':"Настройки печати"
		,'BO1031':"Франшиза"
		,'BO1032':"Бренд"
		,'BO1033':"Магазин"
		,'BO1034':"Настройки печати"
		,'BO1035':"Beacon"
		,'BO1036':"Лицензия POS"
		,'BO1037':"Управление"
		,'BO1038':"Обзор"
		,'BO1039':"Событие"
		,'BO1040':"Уведомление"
		,'BO1041':"Правила"
		,'BO1042':"Версия приложения"
		,'BO1043':"Общий код"
		,'BO1044':"Меню"
		,'BO1045':"Группа пользователей"
		,'BO1046':"Доступ для группы пользователей"
		,'BO1047':"Пользователь"
		,'BO1048':"Маркетинг"
		,'BO1049':"Пользователь"
		,'BO1050':"Рекламный купон"
		,'BO1051':"Выданные купоны"
		,'BO1052':"Сообщение"
		,'BO1053':"Маркетинг через PUSH"
		,'BO1054':"Запасы"
		,'BO1055':"Состояния остатков"
		,'BO1056':"Актуальные остатки"
		,'BO1057':"Импорт остатков регистрации"
		,'BO1058':"Импорт остатков"
		,'BO1059':"Экспорт остатков"
		,'BO1060':"Просмотр остатков"
		,'BO1061':"Поставщики"
		,'BO1062':"Управление продажами"
		,'BO1063':"Анализ управления продажами"
		,'BO1064':"Управление купонами"
		,'BO1065':"Покупатель"
		/* data table title */
		,'BO2001':"Нет"
		,'BO2002':"Корпоративный код"
		,'BO2003':"Корпоративное имя"
		,'BO2004':"Название компании"
		,'BO2005':"Номер компании"
		,'BO2006':"Руководитель"
		,'BO2007':"Управление компанией"
		,'BO2008':"Дата открытия компании"
		,'BO2009':"Дата закрытия компании"
		,'BO2010':"Домашняя страница"
		,'BO2011':"Код страны"
		,'BO2012':"Состояние службы"
		,'BO2013':"Адрес 1"
		,'BO2014':"Адрес 2"
		,'BO2015':"Имя Руководителя"
		,'BO2016':"Номер телефона"
		,'BO2017':"Номер факса"
		,'BO2018':"эл. почта"
		,'BO2019':"Информация о бренде"
		,'BO2020':"Код бренда"
		,'BO2021':"Имя бренда"
		,'BO2022':"Название компании"
		,'BO2023':"Тип компании"
		,'BO2024':"Номер телефона"
		,'BO2025':"Тип обслуживания"
		,'BO2026':"Тип заработка"
		,'BO2027':"Валюта"
		,'BO2028':"Лого бренда"
		,'BO2029':"Значение по умолчанию. Вы можете его изменить"
		,'BO2030':"Обзор"
		,'BO2031':"Используется"
		,'BO2032':"Свободно"
		,'BO2033':"Самостоятельный заказ"
		,'BO2034':"Бронирование"
		,'BO2035':"Заказ банкета"
		,'BO2036':"Свободное время для заказа"
		,'BO2037':"Бронирование депозит / 1 человек"
		,'BO2038':"Заказ банкета депозит / 1 человек"
		,'BO2039':"Код магазина"
		,'BO2040':"Имя магазина"
		,'BO2041':"Область"
		,'BO2042':"Статус операции"
		,'BO2043':"Местонахождение"
		,'BO2044':"Общая информация."
		,'BO2045':"Настройки службы"
		,'BO2046':"Информация о магазине"
		,'BO2047':"Beacon"
		,'BO2048':"Лицензия POS"
		,'BO2049':"Персонал"
		,'BO2050':"Средний чек на одного человека"
		,'BO2051':"Время работы магазина"
		,'BO2052':"Плюсы заведения"
		,'BO2053':"Парковка"
		,'BO2054':"Детское кресло"
		,'BO2055':"Малые залы"
		,'BO2056':"Банкет"
		,'BO2057':"Столы на улице"
		,'BO2058':"Фото заведения"
		,'BO2059':"Выбрать"
		,'BO2060':"ID устройства"
		,'BO2061':"Major"
		,'BO2062':"Minor"
		,'BO2063':"Установить местоположение"
		,'BO2064':"Дата создания"
		,'BO2065':"Лицензионный код"
		,'BO2066':"Вид"
		,'BO2067':"Время использования"
		,'BO2068':"Номер POS"
		,'BO2069':"Сертификат персонала"
		,'BO2070':"Настройки печати"
		,'BO2071':"Количество печатей"
		,'BO2072':"Печать(BG)"
		,'BO2073':"Печать(Штамп)"
		,'BO2074':"Период действия печати"
		,'BO2075':"От даты выпуска"
		,'BO2076':"Название купона"
		,'BO2077':"Условия использования купона"
		,'BO2078':"Сумма купона"
		,'BO2079':"Скидка по купону"
		,'BO2080':"Максимальная сумма"
		,'BO2081':"Срок действия купона"
		,'BO2082':"Примечание к полученным купонам"
		,'BO2083':"Примечание к использованию купона"
		,'BO2084':"Дата обновления"
		,'BO2085':"Статус устройства"
		,'BO2086':"Магазин установлен"
		,'BO2087':"Добавить Beacon"
		,'BO2088':"Обновить Beacon"
		,'BO2089':"Зарегистрировать магазин"
		,'BO2090':"Заголовок"
		,'BO2091':"Период действия"
		,'BO2092':"Изображение"
		,'BO2093':"Содержание"
		,'BO2094':"Время"
		,'BO2095':"Номинальный"
		,'BO2096':"Автор"
		,'BO2097':"Время публикации"
		,'BO2098':"Имя условий"
		,'BO2099':"Тип соглашения"
		,'BO2100':"Версия"
		,'BO2101':"Управление условиями пользования"
		,'BO2102':"Управление событиями"
		,'BO2103':"Управление уведомлениями"
		,'BO2104':"Имя приложения"
		,'BO2105':"Версия приложения"
		,'BO2106':"Тип устройства"
		,'BO2107':"Коментарии приложения"
		,'BO2108':"Дата создания"
		,'BO2109':"Имя проекта"
		,'BO2110':"Имя пакета"
		,'BO2111':"Управление версией приложения"
		,'BO2112':"Код версии"
		,'BO2113':"Настройки обновления"
		,'BO2114':"Обновить записи"
		,'BO2115':"Удалить последние обновления"
		,'BO2116':"Опции"
		,'BO2117':"Управление групповыми кодами"
		,'BO2118':"Код группы"
		,'BO2119':"Исправлено 3-х значное"
		,'BO2120':"Имя группы"
		,'BO2121':"Код модуля"
		,'BO2122':"Тип бизнеса"
		,'BO2123':"Сортировать по частям"
		,'BO2124':"Использовать флаг"
		,'BO2125':"Примечание"
		,'BO2126':"Управление общим кодом"
		,'BO2127':"Общий код"
		,'BO2128':"Подкаттегория кода"
		,'BO2129':"ALIAS"
		,'BO2130':"Управление группой пользователей"
		,'BO2131':"Исправлено 6-х значное"
		,'BO2132':"Имя группы"
		,'BO2133':"Код для групповых ролей"
		,'BO2134':"Управление пользователем"
		,'BO2135':"Имя пользователя"
		,'BO2136':"Номер мобильного телефона"
		,'BO2137':"Управление меню"
		,'BO2138':"Родительское меню"
		,'BO2139':"Добавить дочернее меню"
		,'BO2140':"Код меню"
		,'BO2141':"Заголовок"
		,'BO2142':"Целевой URL"
		,'BO2143':"Примечание"
		,'BO2144':"Выбор участника"
		,'BO2145':"История заработанных штампов"
		,'BO2146':"Заработанные штампы"
		,'BO2147':"Выпущенные купоны"
		,'BO2148':"Управление продвижением купонов"
		,'BO2149':"Имя акции"
		,'BO2150':"Тип купона"
		,'BO2151':"Имя пользователя"
		,'BO2152':"Код пользователя"
		,'BO2153':"Пол"
		,'BO2154':"День рождения"
		,'BO2155':"Детали"
		,'BO2156':"Дата регистрации"
		,'BO2157':"Последний вход"
		,'BO2158':"Компания"
		,'BO2159':"Код купона"
		,'BO2160':"Управление купонами"
		,'BO2161':"Период действия купонов"
		,'BO2162':"Максимальная сумма скидки"
		,'BO2163':"Скидка по сумме \ %"
		,'BO2164':"Использовать разграничитель"
		,'BO2165':"Ограничить использование купона"
		,'BO2166':"Свободные дни"
		,'BO2167':"Свободные часы"
		,'BO2168':"Свободный магазин"
		,'BO2169':"Тим скидки"
		,'BO2170':"Величина скидки"
		,'BO2171':"Период действия"
		,'BO2172':"Подарочный купон"
		,'BO2173':"Заданный период"
		,'BO2174':"Имя купона отображаемое покупателям"
		,'BO2175':"1+1 купон"
		,'BO2176':"Дата выпуска"
		,'BO2177':"Дата присоединения"
		,'BO2178':"Реклама отправлена"
		,'BO2179':"Выпущенная история"
		,'BO2180':"Выпущенные детали"
		,'BO2181':"Неограниченный"
		,'BO2182':"Ограниченный"
		,'BO2183':"Все магазины"
		,'BO2184':"Выбранные магазины"
		,'BO2185':"Разрешенный"
		,'BO2186':"Не разрешенный"
		,'BO2187':"Подробнее о купоне"
		,'BO2188':"Бесплатный купон"
		,'BO2189':"Купон сформирован"
		,'BO2190':"Цель установлена"
		,'BO2191':"Вссе покупатели"
		,'BO2192':"Выбор группы"
		,'BO2193':"Прямой ввод"
		,'BO2194':"Информация о покумателе"
		,'BO2195':"Штамп получен"
		,'BO2196':"Купон получен\использован"
		,'BO2197':"Возраст"
		,'BO2198':"менее 10"
		,'BO2199':"от 20"
		,'BO2200':"от 30"
		,'BO2201':"от 40"
		,'BO2202':"от 50"
		,'BO2203':"от 60 и старше"
		,'BO2204':"Целевые магазины"
		,'BO2205':"Выбранные магазины"
		,'BO2206':"Детали акции"
		,'BO2207':"Выберите рекламный купон"
		,'BO2208':"Выпуск по графику"
		,'BO2209':"Немедленный выпуск"
		,'BO2210':"Отложенный выпуск"
		,'BO2211':"Завершить после отложенного выпуска"
		,'BO2212':"Перейти к маркетингу после отложенного выпуска"
		,'BO2213':"Управление категориями"
		,'BO2214':"Имя меню"
		,'BO2215':"Сумма продаж"
		,'BO2216':"Флаг продаж"
		,'BO2217':"Управление товарами"
		,'BO2218':"Рекомендованный размер"
		,'BO2219':"Имя меню(сокращенное)"
		,'BO2220':"Регистрация категории"
		,'BO2221':"Продукты категории"
		,'BO2222':"Продажи категории"
		,'BO2223':"Налоги категории"
		,'BO2224':"Налоги"
		,'BO2225':"Дьюти фри"
		,'BO2226':"Период продаж"
		,'BO2227':"Изменить тип обслуживания"
		,'BO2228':"Примененный"
		,'BO2229':"Не используемый"
		,'BO2230':"иконка мобильного"
		,'BO2231':"НОВИНКА"
		,'BO2232':"ЛУЧШЕЕ"
		,'BO2233':"Отметка о заработанных наградах"
		,'BO2234':"Заработанная цель"
		,'BO2235':"Не заработано"
		,'BO2236':"Доступные пакет флага"
		,'BO2237':"Доступные пакеты"
		,'BO2238':"Недопустимый пакет"
		,'BO2239':"Опции имени"
		,'BO2240':"Заказ"
		,'BO2241':"Необходимые"
		,'BO2242':"Максимальный выбор"
		,'BO2243':"Имя позиции"
		,'BO2244':"Добавленная сумма"
		,'BO2245':"Удалить"
		,'BO2246':"Тип категории"
		,'BO2247':"Перечень категорий"
		,'BO2248':"Все категории"
		,'BO2249':"Продано"
		,'BO2250':"Этаж"
		,'BO2251':"1эт, 2эт, ……. 20эт"
		,'BO2252':"Изменить количество столов"
		,'BO2253':"Удалить этаж"
		,'BO2254':"Изменить количество"
		,'BO2255':"Изменить"
		,'BO2256':"Записка повару"
		,'BO2257':"Заработанные продажи"
		,'BO2258':"Отмененные продажи"
		,'BO2259':"Средний заработок за день"
		,'BO2260':"Заработано всего"
		,'BO2261':"Информация о штампе"
		,'BO2262':"Дата \ время"
		,'BO2263':"Номер чека"
		,'BO2264':"Нормальный"
		,'BO2265':"Статус выданного купона"
		,'BO2266':"Смотреть все купоны"
		,'BO2267':"Категория выдачи"
		,'BO2268':"Включенный период действия"
		,'BO2269':"Статус купона"
		,'BO2270':"Использовать купон"
		,'BO2271':"Нормальный штамп"
		,'BO2272':"Категория брендов"
		,'BO2273':"Стоимость купона для категории"
		,'BO2274':"Соотношение Бренда %"
		,'BO2275':"Сегодня продано"
		,'BO2276':"Сегодня обслужено"
		,'BO2277':"Штампы за сегодня"
		,'BO2278':"Всего"
		,'BO2279':"Заказ"
		,'BO2280':"Средний"
		,'BO2281':"Самостоятельный заказ"
		,'BO2282':"Бронирование"
		,'BO2283':"Заказ банкета"
		,'BO2284':"Штамп"
		,'BO2285':"Выпущенные кreg"
		,'BO2286':"Использованные купоны"
		,'BO2287':"Популярные покупки"
		,'BO2288':"ТОП 5 заказов сегодня"
		,'BO2289':"Меньше всего заказывают эти 5 позиций"
		,'BO2290':"ТОП 5 позиций"
		,'BO2291':"ТОП 5 категорий"
		,'BO2292':"Сегодня покупателей"
		,'BO2293':"Последние просмотренные"
		,'BO2294':"Сегодняшние печати"
		,'BO2295':"Выпущено купонов за неделю"
		,'BO2296':"Использовано купонов за неделю"
		,'BO2297':"Мой магазин (сегодня)"
		,'BO2298':"Сегодня обслужено"
		,'BO2299':"ТОП 5 позиций неделю"
		,'BO2300':"ТОП 5 категорий неделю"
		,'BO2301':"Полученные сегодня оплаты"
		,'BO2302':"Новые покупатели VS возвращенные покупатели / За последние 30 дней"
		,'BO2303':"Сумма продаж"
		,'BO2304':"Актуальная сумма продаж"
		,'BO2305':"Дополнительный налог"
		,'BO2306':"Сумма дополнительного налога"
		,'BO2307':"Количество посетителей"
		,'BO2308':"Всего"
		,'BO2309':"Банковская карта"
		,'BO2310':"Наличные"
		,'BO2311':"Прочее"
		,'BO2312':"Количество Продаж"
		,'BO2313':"Количество заказов"
		,'BO2314':"Среднее число продаж"
		,'BO2315':"Дни / Недели / Месяцы"
		,'BO2316':"Офлайн"
		,'BO2317':"Метод оплаты"
		,'BO2318':"Количество оплат"
		,'BO2319':"Часы"
		,'BO2320':"Дни"
		,'BO2321':"Среднее число продаж за день"
		,'BO2322':"Номер Заказа"
		,'BO2323':"Дата продажи"
		,'BO2324':"Время продажи"
		,'BO2325':"Номер подтверждения"
		,'BO2326':"Количество"
		,'BO2327':"Количество подвтерждений"
		,'BO2328':"Номер банковской карты"
		,'BO2329':"Взнос"
		,'BO2330':"Компания покупатель"
		,'BO2331':"Дата подтверждения"
		,'BO2332':"Время подтверждения"
		,'BO2333':"Статус"
		,'BO2334':"Установщик"
		,'BO2335':" CSV файл"
		,'BO2336':"Разрешенный флаг для сохранения специфичных настроек магазина"
		,'BO2337':"Количество купонов"
		,'BO2338':"Минимальная установка"
		,'BO2339':"Переустановка"
		,'BO2340':"Камеры наблюдения"
		,'BO2341':"Тип магазина"
		,'BO2342':"Номер мобильного телефона"
		,'BO2343':"(координаты / поиск адреса)"
		,'BO2344':"Добавить файл"
		,'BO2345':"ID"
		,'BO2346':"Пароль"
		,'BO2347':"Имя сотрудника"
		,'BO2348':"Домен"
		,'BO2349':"Порт"
		,'BO2350':"Выбранная позиция"
		,'BO2351':"Сумма"
		,'BO2352':"Дневной"
		,'BO2353':"Недельный"
		,'BO2354':"Месячный"
		,'BO2355':"Отменить купон"
		,'BO2356':"Дата создания"
		,'BO2357':"Зарегистрирован"
		,'BO2358':"Период добавления"
		,'BO2359':"Содержание"
		,'BO2360':"Обработка"
		,'BO2361':"Конец"
		,'BO2362':"Обязательно для принятия"
		,'BO2363':"Согласие по желанию"
		,'BO2364':"Тип правил"
		,'BO2365':"Условия предоставления услуг"
		,'BO2366':"Политика безопасности"
		,'BO2367':"Общая информация о безопасности"
		,'BO2368':"PUSH"
		,'BO2369':"СМС"
		,'BO2370':"Beacon"
		,'BO2371':"Код заголовка"
		,'BO2372':"Дата создания меню"
		,'BO2373':"Дата выдачи"
		,'BO2374':"Цель меню"
		,'BO2375':"Пользовательское меню"
		,'BO2376':"Выберите подразделение"
		,'BO2377':"Управление"
		,'BO2378':"Код"
		,'BO2379':"Количество столов"
		,'BO2380':"Название зала/этажа"
		,'BO2381':"Название стола"
		,'BO2382':"Выберите группу пользователей"
		,'BO2383':"Тип пользователя"
		,'BO2384':"Группа пользователей"
		,'BO2385':"Подразделение"
		,'BO2386':"Все услуги"
		,'BO2387':"Все статусы"
		,'BO2388':"Обычный"
		,'BO2389':"Отменённый"
		,'BO2390':"Цена"
		,'BO2391':"Карты+Нал."
		,'BO2392':"Детали"
		,'BO2393':"Статус продаж"
		,'BO2394':"Дополнительные параметры имени"
		,'BO2395':"История последних штампов"
		,'BO2396':"Штамп"
		,'BO2397':"Дата выпуска"
		,'BO2398':"Статус купона"
		,'BO2399':"Получить статус"
		,'BO2400':"Получить Division"
		,'BO2401':"Получить купон"
		,'BO2402':"Рекламируемые купоны"
		,'BO2403':"Отменить выпуск купона"
		,'BO2404':"Приостановить действие купона"
		,'BO2405':"Статус заказа"
		,'BO2406':"Купон выпущенный заведением"
		,'BO2407':"Купон используемый заведением"
		,'BO2408':"Период акции"
		,'BO2409':"Выберите купон для акции"
		,'BO2410':"Последняя дата входа"
		,'BO2411':"Неиспользуемый"
		,'BO2412':"За последнюю неделю"
		,'BO2413':"За последние 2 недели"
		,'BO2414':"За последний месяц"
		,'BO2415':"За последние 3 месяца"
		,'BO2416':"За последние 6 месяцев"
		,'BO2417':"За последний год"
		,'BO2418':"Заказ"
		,'BO2419':"Статус выпуска"
		,'BO2420':"Время выпуска"
							,'BO2421':"Promotion No"
							,'BO2422':"Expire Date"
							,'BO2423':"Total Order Amount"
							,'BO2424':"Issued Count"
		,'BO2425':"включая"
		,'BO2426':"без"
		,'BO2427':"Видеорекордер"
		,'BO2428':"имя камеры"
		,'BO2429':"номер камеры"
		,'BO2430':"модель камеры"
							,'BO2431':"Discount Rate"
							,'BO2432':"Maximum Discount Rate"
							,'BO2433':"Category code"
							,'BO2434':"Choose File"
		,'BO2435':"Кол-во использованных купонов"
		,'BO2436':"Кол-во используемых купонов"
		,'BO2437':"Расчётная сумма"
		,'BO2438':"Ожидаемые продажи"
		,'BO2439':"Эффективность продаж"
		,'BO2440':"Скорость достижения"
		,'BO2441':"Месяц"
		,'BO2442':"Количество продаж"
		,'BO2443':"Цена продажи"
		,'BO2444':"Доступный флаг запасов"
		,'BO2445':"Сохранённый флаг запасов"
		,'BO2446':"рецепт"
		,'BO2447':"Онлайн"
		,'BO2448':"Офлайн"
							,'BO2449':'Membership'
		,'BO2450':"Налог"
		,'BO2451':"Информация о франшизе"
		,'BO2452':"Код категории"
		,'BO2453':"Имя категории"
		,'BO2454':"Опции импорта"
		,'BO2455':"Тип продаж"
		,'BO2456':"Принтер"
		,'BO2457':"Тип"
		,'BO2458':"Код компании"
		,'BO2459':"Имя"
		,'BO2460':"Номер POS:"
		,'BO2461':"Номер принтера:"
		,'BO2462':"Тип принтера"
		,'BO2463':"Принтер кухни"
		,'BO2464':"Чековый принтер"
		,'BO2465':"Код принтера"
		,'BO2466':"IP"
		,'BO2467':"IP порт"
		,'BO2468':"MAC адрес"
		,'BO2469':"Файл устройства"
		,'BO2470':"Имя устройства"
		,'BO2471':"BITS"
		,'BO2472':"Parity"
		,'BO2473':"Stop BITS"
		,'BO2474':"Тип порта"
		,'BO2475':"Авто-отключение bluetooth"
		,'BO2476':"Авто-включение bluetooth"
		,'BO2477':"Тип соединения"
		,'BO2478':"Скорость"
		,'BO2479':"Кол-во принтеров"
		,'BO2480':"USB VID"
		,'BO2481':"USB PID"
		,'BO2482':"Протокол USB"
		,'BO2483':"Пароль доступа"
		,'BO2484':"Пароль доступа пользователя"
		,'BO2485':"Тип модели"
		,'BO2486':"График по часам"
		,'BO2487':"График по дням"
		,'BO2488':"График по неделям"
		,'BO2489':"График по месяцам"
		,'BO2490':"Время дня"
		,'BO2491':"По дням месяца"
		,'BO2492':"По дням недели"
		,'BO2493':"По месяцам"
		,'BO2494':"ТОП 5 продаж"
		,'BO2495':"Топ 5 категорий"
		,'BO2496':'TimeZone'
		,'BO2497':"Пожалуйста введите информацию о процессинге для совершения оплаты из мобильного"
		,'BO2498':"ID заведения"
		,'BO2499':"Private Security Key"
		,'BO2500':"Payment Key"
		,'BO2501':"Теже настройки, что и для Бренда"
		,'BO2502':"Auth. Display"
		,'BO2503':"Админ"
		,'BO2504':"Магазин"
		,'BO2505':"ШОУ В APP"
		/* button event */
		,'BO3001':"Сегодня"
		,'BO3002':"Вчера"
		,'BO3003':"Последние 7 дней"
		,'BO3004':"Последние 30 дней"
		,'BO3005':"Этот месяц"
		,'BO3006':"Последний месяц"
		,'BO3007':"Этот год"
		,'BO3008':"Последний год"
		,'BO3009':"Произвольный период"
		,'BO3010':"Г."
		,'BO3011':"М."
		,'BO3012':"Д."
		,'BO3013':"Янв."
		,'BO3014':"Фев."
		,'BO3015':"Мар."
		,'BO3016':"Апр."
		,'BO3017':"Май"
		,'BO3018':"Июнь"
		,'BO3019':"Июль"
		,'BO3020':"Авг."
		,'BO3021':"Сен."
		,'BO3022':"Окт."
		,'BO3023':"Ноя."
		,'BO3024':"Дек."
		,'BO3025':"Пон."
		,'BO3026':"Вт."
		,'BO3027':"Ср."
		,'BO3028':"Чет."
		,'BO3029':"Пт."
		,'BO3030':"Суб."
		,'BO3031':"Воск."
		,'BO3032':"Начало даты"
		,'BO3033':"Конец даты"
		,'BO3034':"Произвольный период"
		,'BO3035':"Добавить новый"
		,'BO3036':"Закрыть всё"
		,'BO3037':"Развернуть всё"
		,'BO3038':"Импорт"
		,'BO3039':"Добавить тип"
		,'BO3040':"Удалить всё"
		,'BO3041':"Удалить выбранное"
		,'BO3042':"Добавить список"
		,'BO3043':"Изменить выделенные"
		,'BO3044':"Отменить заработанный купон"
		,'BO3045':"Отменить выданный"
		,'BO3046':"Отменить использованный"
		,'BO3047':"Устанавливать"
		,'BO3048':"Изменить координаты"
		,'BO3049':"Добавить Beacon"
		,'BO3050':"Удалить Beacon"
		,'BO3051':"Удалить POS"
		,'BO3052':"Добавить персонал"
		,'BO3053':"Удалить персонал"
		,'BO3054':"Удалить"
		,'BO3055':"Индивидуальная регистрация"
		,'BO3056':"Массовая регистрация"
		,'BO3057':"Отправить письмо"
		,'BO3058':"Отправить смс"
		,'BO3059':"отправить push"
		,'BO3060':"Использовать выбранный маркетинг"
		,'BO3061':"Выдать купон тем, кто ещё не получал их"
		,'BO3062':"Выбор завершен"
		,'BO3063':"Загрузить Excel"
		,'BO3064':"Скачать образец"
		,'BO3065':"Удалить файл"
		,'BO3066':"Экспорт .csv"
		,'BO3067':"Сохранить опции&заказы"
		,'BO3068':"(прим. : 8 (495) 123-45-67)"
		,'BO3069':"(прим. : 10-1234-5678)"
		,'BO3070':"Пакетная регистрация"
		/* alert, message event*/
		,'BO4001':"Пожалуйста выберите запись"
		,'BO4002':"Пожалуйста выберите бренд"
		,'BO4003':"Пожалуйста выберите Магазин"
		,'BO4004':"В первую очередь сохраните добавленную информацию"
		,'BO4005':"Информация о разделах будет зарегистрирована после выбора магазина"
		,'BO4006':"Пожалуйста введите информацию о категории"
		,'BO4007':"Пожалуйста введите код категории"
		,'BO4008':"Изображение не зарегистрировано"
		,'BO4009':"Файл с изображением не добавлен"
		,'BO4010':"Допускаются только gif, png, jpg, jpeg"
		,'BO4011':"Ранее это уже было добавлено"
		,'BO4012':"Пожалуйста прикрепите CSV файл"
		,'BO4013':"Вы хотите загрузить?"
		,'BO4014':"Вы уверены что хотите избавиться от этого?"
		,'BO4015':"Вы уверены что хотите удалить это?"
		,'BO4016':"Дубликаты ID устройства (UUID&Major&Minor)"
		,'BO4017':"Не удалось загрузить CSV! Дубликат устройства ID"
		,'BO4018':"Дубликат кода бренда"
		,'BO4019':"Дубликат кода"
		,'BO4020':"Вы ме можете установить соответствующие бренды магазинов"
		,'BO4021':"Штамп (фоновое изображение) не удалось зарегистрировать"
		,'BO4022':"Штамп (печать) не удалось зарегистрировать изображение"
		,'BO4023':"Beacon уже добавлен"
		,'BO4024':"POS лицензия уже добавлена"
		,'BO4025':"Позиции не выбраны"
		,'BO4026':"Регистрация не удалась"
		,'BO4027':"Дубликат кода магазина"
		,'BO4028':"Пожалуйста добавьте местонахождение"
		,'BO4029':"Удалено успешно"
		,'BO4030':"Ничего не найдено"
		,'BO4031':"Пожалуйста выберите позиции"
		,'BO4032':"Пожалуйста выберите период времени"
		,'BO4033':"Поиск доступных позиций"
		,'BO4034':"Все"
		,'BO4035':"Запрос"
		,'BO4036':"Регистрация"
		,'BO4037':"Изменить"
		,'BO4038':"Удалить"
		,'BO4039':"Вывод"
		,'BO4040':"имя кода"
		,'BO4041':"Код группы"
		,'BO4042':"Дата"
		,'BO4043':"Час"
		,'BO4044':"Пожалуйста нажмите."
		,'BO4045':"Выберите категорию."
		,'BO4046':'Сохранение завершено.'
		,'BO4047':'Ошибка сохранения.'
		,'BO4049':'Удаление завершено.'
		,'BO4050':"Ошибка удаления."
		,'BO4051':"Нет информации о позиции."
		,'BO4052':"Введите название."
		,'BO4053':"Подтвердите максимальный выбор"
		,'BO4054':"Выберите опции"
		,'BO4055':"Выберите тип категории."
		,'BO4056':"Введите записку для кухни."
		,'BO4057':"Выберите главную категорию."
		,'BO4058':"Выберите назначение."
		,'BO4059':"Ошибка добавления главного изображения."
		,'BO4060':"Вы действительно хотите выйти?"
		,'BO4061':"Не удалось загрузить CSV! Дубликат лицензии"
		,'BO4062':'Вы не можете отменить купон выпущенный ранее.'
		,'BO4063':'Пожалуйста выберите файл.'
		,'BO4064':'Пожалуйста введите Settlement rate.'
		,'BO4065':'Отправить {0} to {1} покупателям.'
		/* BO5001 ~ BO5999 */
		,'BO5001':"Код корпорации"
		,'BO5002':"Имя корпорации"
		,'BO5003':"Сохранить остатки"
		,'BO5004':"Текущие остатки"
		,'BO5005':"Актуальные остатки"
		,'BO5006':"Gap"
		,'BO5007':"Базовое количество"
		,'BO5008':"Импорт количества"
		,'BO5009':"Экспорт количества"
		,'BO5010':"Корректировка количества"
		,'BO5011':"Количество запасов"
		,'BO5012':"Поставщики"
		,'BO5013':"Цена за единицу"
		,'BO5014':"Количество"
		,'BO5015':"Итого:"
		,'BO5016':"Корректировка количества"
		,'BO5017':"Исправить дату"
		,'BO5018':"Экспортировать дату"
		,'BO5019':"Импортировать дату"
		,'BO5020':"Импорт в .csv"
		,'BO5021':"Скачать csv"
		,'BO5022':"Остановить продажи"
		,'BO5023':"Всё меню"
		,'BO5024':"Все запасы"
		,'BO5025':"Под сохраненными запасами"
		,'BO5026':"0 или больше"
		,'BO5027':"Менее чем 0"
});