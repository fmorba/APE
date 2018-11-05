package modelos;

public class ModeloEvento {
    String nombreEvento;
    String fechaEvento;
    String horaInicioEvento;
    String horaFinEvento;
    String descripcionEvento;
    int recordatorioEvento;
    int idUsuario;

    public ModeloEvento(String nombreEvento, String fechaEvento, String horaInicioEvento, String horaFinEvento, String descripcionEvento, boolean recordatorioEvento, int idUsuario) {
        this.nombreEvento = nombreEvento;
        this.fechaEvento = fechaEvento;
        this.horaInicioEvento = horaInicioEvento;
        this.horaFinEvento = horaFinEvento;
        this.descripcionEvento = descripcionEvento;
        // SQl no posee una clase booleano, en su lugar utiliza 1 y 0.
        if (recordatorioEvento) {
            this.recordatorioEvento= 1; //Hay que recordar el evento.
        }else{
            this.recordatorioEvento = 0; //No es necesario recordar el evento.
        }
        this.idUsuario = idUsuario;
    }

    public ModeloEvento(String nombreEvento, String fechaEvento, String horaInicioEvento, String horaFinEvento, String descripcionEvento, int recordatorioEvento, int idUsuario) {
        this.nombreEvento = nombreEvento;
        this.fechaEvento = fechaEvento;
        this.horaInicioEvento = horaInicioEvento;
        this.horaFinEvento = horaFinEvento;
        this.descripcionEvento = descripcionEvento;
        this.recordatorioEvento=recordatorioEvento;
        this.idUsuario = idUsuario;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public String getFechaEvento() {
        return fechaEvento;
    }

    public String getHoraInicioEvento() {
        return horaInicioEvento;
    }

    public String getHoraFinEvento() {
        return horaFinEvento;
    }

    public String getDescripcionEvento() {
        return descripcionEvento;
    }

    public int getRecordatorioEvento() {
        return recordatorioEvento;
    }

    public int getIdUsuario() {
        return idUsuario;
    }


}
