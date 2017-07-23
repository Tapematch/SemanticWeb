PREFIX sw: <https://gitlab.hrz.tu-chemnitz.de/ptaubert--htwk-leipzig.de/SemanticWeb#>
SELECT ?event ?beginn ?interpret ?location ?adresse ?seconds ?entfernung
    WHERE {
        ?interpret sw:besitzt <https://gitlab.hrz.tu-chemnitz.de/ptaubert--htwk-leipzig.de/alternative+rock> ;
            sw:spielt_auf ?event .
        ?event sw:findet_statt_bei ?location ;
            sw:beginn ?beginn .
        ?location sw:hat ?anfahrtsdauer .
        ?anfahrtsdauer sw:anfahrtsdauer ?seconds ;
            sw:endadresse ?adresse ;
            sw:entfernung ?entfernung .
            FILTER (?seconds <= 7200)
}