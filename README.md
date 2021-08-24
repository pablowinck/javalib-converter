# Converter
Conversor de Date, Txt e Json para Java
<br>
### Exemplos de uso
#### toTxt
`Converter.toTxt(Converter.DIRETORIO_DOCUMENTOS, "Nome do Programa", "Nome do Txt", "Conteúdo do TXT");` <br>
#### fromTxt
`String conteudoTxt = Converter.fromTxt(Converter.DIRETORIO_DOCUMENTOS, "Nome do Programa", "Nome do Txt");` <br>
#### toDate (DATE)
`Date data = Converter.toDate("24/02/2000", false); // o formato do Date é independente, pode ser 2000-02-24, por exemplo.` 
#### toDate (DATETIME)
`Date data = Converter.toDate("24/02/2000 16:30", true); // o formato do Date é independente, pode ser 2000-02-24 16:30, por exemplo.` 
#### toJson (Usa a lib GSON para fazer a conversão)
*Object:*<br>
`String json = Converter.toJson(new Pessoa(1, "Pablo")); // o formato do Date é independente, pode ser 2000-02-24 16:30, por exemplo.
// resultado: {"id":1,"nome":"Pablo"}`<br>
*List:*<br>
`String json = Converter.toJson(Arrays.asList(new Pessoa(1, "Pablo"), new Pessoa(2, "Fulano"), new Pessoa(3, "Ciclano")));
// resultado: [{"id":1,"nome":"Pablo"},{"id":2,"nome":"Fulano"},{"id":3,"nome":"Ciclano"}]` 
#### fromJson (LIST)
`List<Pessoa> lista = Converter.fromJson(json, new TypeToken<ArrayList<Pessoa>>(){});`
#### fromJson (OBJECT)
`Pessoa pessoa = Converter.fromJson(json, Pessoa.class);`
