package br.com.teste.piorfilme.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import br.com.teste.piorfilme.dto.ResultadoIntervalosDTO;
import br.com.teste.piorfilme.entity.Filme;
import br.com.teste.piorfilme.repositories.FilmeRepository;
import br.com.teste.piorfilme.service.FilmeService;

@RestController
public class FilmeController {
	@Autowired
	FilmeRepository service;
	@Autowired
	FilmeService filmeService;
	
	@PostMapping("/upload")
	public String salvar(@RequestParam("file") MultipartFile file) throws Exception {
		List<Filme> filmesList = new ArrayList<>();
		InputStream inputStream = file.getInputStream();
		CsvParserSettings settings = new CsvParserSettings();
		settings.setHeaderExtractionEnabled(true);
		settings.getFormat().setDelimiter(';');
		CsvParser parser  = new CsvParser(settings);

		List<Record> parserAllRecords = parser.parseAllRecords(inputStream);
		// 🔍 LOG: Verificar se leu registros
		System.out.println("Total de registros lidos: " + parserAllRecords.size());
		if (!parserAllRecords.isEmpty()) {
			String[] headers = parserAllRecords.get(0).getMetaData().headers();
			System.out.println("Colunas do CSV: " + String.join(", ", headers));
		}
		parserAllRecords.forEach(record ->{
			Filme filme = new Filme();
			filme.setAno(Integer.parseInt(record.getString("ano")));
			filme.setNome(record.getString("nome"));
			filme.setProdutor(record.getString("produtor"));
			String venceuStr = record.getString("venceu");
			filme.setVenceu(venceuStr != null && venceuStr.trim().equals("1"));
			filmesList.add(filme);
			
		});
		service.saveAll(filmesList);
		return "Arquivo carregado com sucesso!!!";
	}
	@GetMapping("/produtores/intervalos") 
	public ResultadoIntervalosDTO getIntervalosProdutores() {
	    
	    return filmeService.buscarIntervalosProdutores();
	}
}