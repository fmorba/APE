# APE - Agenda y Planificador de Estudio
Resumen del versionado:

Version 0.1: Se programaron las clases basicas definidas en el analisis previo con el fin de tener la estructura basica de la aplicacion, la interfaz de usuario es navegable y permite interactuar con ella, haciendo de esta version un prototipo funcional de la misma. Se armo la estructura para una base de datos SQLite, esta ultima caracteristica no esta definida como permanente y podra ser remplazada en futuras versiones.

Version 0.2: Se generaron las clases relacionadasa la identificacion y registro de los usuarios, asi como la administracion de los eventos pertenecientes a la agenda. Luego de probar la base de datos SQLite, y por consejos externos, se decidio utilizar una base de datos online, la misma es solo un prototipo y probablemente sera cambiada por una version de mejor calidad.

Version 0.3: Se termino de programar las funcionalidades relacionadas con la gestion de registros de materias y examenes, los cuales son funcionalidades accesorias de la agenda, dichas funcionalidades estan activas y probadas. Tambien se dejo de lado la base de datos online, y se decidio implementar Firebase, debido a los beneficios que dicho servicio incluye. Por lo mismo, se realizaron las modificaciones y los cambios necesarios, en las clases correspondiente ccon el manejo y transmición de datos, hacia la base de datos externa. Despues de esta iteracion, no se planea realizar mas cambios con respecto a la base de datos externa.