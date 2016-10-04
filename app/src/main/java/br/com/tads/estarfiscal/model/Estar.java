        package br.com.tads.estarfiscal.model;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class Estar {

    @SerializedName("idEstar")
    @Expose
    private String idEstar;
    @SerializedName("placa")
    @Expose
    private String placa;
    @SerializedName("inicio")
    @Expose
    private String inicio;
    @SerializedName("horas")
    @Expose
    private String horas;
    @SerializedName("alerta")
    @Expose
    private Object alerta;
    @SerializedName("Usuario_id")
    @Expose
    private String usuarioId;
    @SerializedName("valor")
    @Expose
    private Object valor;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("Endereco_idEndereco")
    @Expose
    private String enderecoIdEndereco;
    @SerializedName("numero")
    @Expose
    private Object numero;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("situacao")
    @Expose
    private String situacao;
    @SerializedName("diff")
    @Expose
    private String diff;
    @SerializedName("vencido")
    @Expose
    private String vencido;

    /**
     *
     * @return
     * The idEstar
     */
    public String getIdEstar() {
        return idEstar;
    }

    /**
     *
     * @param idEstar
     * The idEstar
     */
    public void setIdEstar(String idEstar) {
        this.idEstar = idEstar;
    }

    /**
     *
     * @return
     * The placa
     */
    public String getPlaca() {
        return placa;
    }

    /**
     *
     * @param placa
     * The placa
     */
    public void setPlaca(String placa) {
        this.placa = placa;
    }

    /**
     *
     * @return
     * The inicio
     */
    public String getInicio() {
        return inicio;
    }

    /**
     *
     * @param inicio
     * The inicio
     */
    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    /**
     *
     * @return
     * The horas
     */
    public String getHoras() {
        return horas;
    }

    /**
     *
     * @param horas
     * The horas
     */
    public void setHoras(String horas) {
        this.horas = horas;
    }

    /**
     *
     * @return
     * The alerta
     */
    public Object getAlerta() {
        return alerta;
    }

    /**
     *
     * @param alerta
     * The alerta
     */
    public void setAlerta(Object alerta) {
        this.alerta = alerta;
    }

    /**
     *
     * @return
     * The usuarioId
     */
    public String getUsuarioId() {
        return usuarioId;
    }

    /**
     *
     * @param usuarioId
     * The Usuario_id
     */
    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    /**
     *
     * @return
     * The valor
     */
    public Object getValor() {
        return valor;
    }

    /**
     *
     * @param valor
     * The valor
     */
    public void setValor(Object valor) {
        this.valor = valor;
    }

    /**
     *
     * @return
     * The latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     *
     * @param latitude
     * The latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return
     * The longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     *
     * @param longitude
     * The longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     *
     * @return
     * The enderecoIdEndereco
     */
    public String getEnderecoIdEndereco() {
        return enderecoIdEndereco;
    }

    /**
     *
     * @param enderecoIdEndereco
     * The Endereco_idEndereco
     */
    public void setEnderecoIdEndereco(String enderecoIdEndereco) {
        this.enderecoIdEndereco = enderecoIdEndereco;
    }

    /**
     *
     * @return
     * The numero
     */
    public Object getNumero() {
        return numero;
    }

    /**
     *
     * @param numero
     * The numero
     */
    public void setNumero(Object numero) {
        this.numero = numero;
    }

    /**
     *
     * @return
     * The address
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @param address
     * The address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     *
     * @return
     * The situacao
     */
    public String getSituacao() {
        return situacao;
    }

    /**
     *
     * @param situacao
     * The situacao
     */
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    /**
     *
     * @return
     * The diff
     */
    public String getDiff() {
        return diff;
    }

    /**
     *
     * @param diff
     * The diff
     */
    public void setDiff(String diff) {
        this.diff = diff;
    }

    /**
     *
     * @return
     * The vencido
     */
    public String getVencido() {
        return vencido;
    }

    /**
     *
     * @param vencido
     * The vencido
     */
    public void setVencido(String vencido) {
        this.vencido = vencido;
    }

}