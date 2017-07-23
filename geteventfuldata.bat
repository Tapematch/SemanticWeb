for /l %%x in (1, 1, 13) do (
   echo %%x
   powershell -Command "Invoke-WebRequest http://api.eventful.com/rest/events/search?app_key=cDjpmDPHhGGFt89j'&'location=Germany'&'date=2017073000-2017092400'&'category=music'&'page_size=250'&'page_number=%%x -OutFile D:\Benutzer\philt\Documents\HTWK\SemanticWeb\data\eventful\page%%x.json"
)
