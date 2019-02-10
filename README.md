# APE - Agenda y Planificador de Estudio
Resumen del versionado:

Versi�n 0.1: Se programaron las clases b�sicas definidas en el an�lisis previo con el fin de tener la estructura b�sica de la aplicaci�n, la interfaz de usuario es navegable y permite interactuar con ella, haciendo de esta versi�n un prototipo funcional de la misma. Se arm� la estructura para una base de datos SQLite, esta �ltima caracter�stica no est� definida como permanente y podr� ser remplazada en futuras versiones.

Versi�n 0.2: Se generaron las clases relacionadas a la identificaci�n y registro de los usuarios, as� como la administraci�n de los eventos pertenecientes a la agenda. Luego de probar la base de datos SQLite, y por consejos externos, se decidi� utilizar una base de datos online, la misma es solo un prototipo y probablemente ser� cambiada por una versi�n de mejor calidad.

Versi�n 0.3: Se termin� de programar las funcionalidades relacionadas con la gesti�n de registros de materias y ex�menes, los cuales son funcionalidades accesorias de la agenda, dichas funcionalidades est�n activas y probadas. Tambi�n se dej� de lado la base de datos online, y se decidi� implementar Firebase, debido a los beneficios que dicho servicio incluye. Por lo mismo, se realizaron las modificaciones y los cambios necesarios, en las clases correspondiente con el manejo y transmisi�n de datos, hacia la base de datos externa. Despu�s de esta iteraci�n, no se planea realizar m�s cambios con respecto a la base de datos externa.

Versi�n 0.4: Se codifico e implemento tanto el algoritmo gen�tico, como el planificador de horarios de estudio, se verifico su funcionamiento, y los resultados obtenidos estuvieron dentro de los rangos aceptables, tambi�n se program� las clases necesarias para el completo y correcto accionar de dichas funcionalidades, as� como tambi�n se program� las relaciones con las clases y caracter�sticas ya existentes en la aplicaci�n, que fueran vinculadas con la actividad del planificador, como es el caso de la agenda.

Versi�n 0.5: Se implementaron las funcionalidades relevantes a la generaci�n de m�tricas, as� como la captura, almacenamiento y gesti�n de archivos. En la primera funcionalidad descripta se program� el c�lculo de una serie de valor que fueran �tiles o interesantes para los usuarios, en base a la informaci�n brindada por los mismos, o a sus pedidos directos, dichos c�lculos se realizan de forma inmediata en base a la informaci�n almacenada, y a su vez incluyen la capacidad de obtener m�tricas particulares seg�n las necesidades de los usuarios. La segunda funcionalidad implementada, es decir, la gesti�n de archivos, resulto m�s dif�cil de programar debido a la falta de experiencia, pero se logr� mantener dentro de los tiempos del cronograma, y permite la captura y almacenaje de archivos de audio y video, as� como notas y fotograf�as. Tambi�n se realizaron correcciones menores al c�digo de la aplicaci�n.

Versi�n 1.0: Esta es la primera versi�n que puede considerarse completa y lista para utilizar, esta versi�n es el entregable producto de la �ltima iteraci�n del proceso de desarrollo, se han realizado m�ltiples pruebas de funcionamiento y rendimiento, con el fin de detectar errores que hayan quedado de las etapas previas y corregirlos. Tambi�n se trabaj� sobre el aspecto visual de la aplicaci�n, agregando los iconos y dem�s recursos gr�ficos.
