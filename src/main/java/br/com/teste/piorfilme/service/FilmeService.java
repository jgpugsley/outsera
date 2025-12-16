package br.com.teste.piorfilme.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.teste.piorfilme.dto.FilmeDTO;
import br.com.teste.piorfilme.dto.IntervaloDTO;
import br.com.teste.piorfilme.dto.ResultadoIntervalosDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Service
public class FilmeService {

    @Autowired
    private EntityManager entityManager;
    
    

    /**
     * Retorna todos os filmes vencedores de produtores que ganharam múltiplos prêmios
     */
    @SuppressWarnings("unchecked")
    public List<FilmeDTO> buscarProdutoresComMultiplosPremioss() {
        String sql = """
            SELECT f.ano, f.produtor, f.nome
            FROM Filme f
            WHERE f.produtor IN (
                SELECT f2.produtor 
                FROM Filme f2
                WHERE f2.venceu = TRUE
                GROUP BY f2.produtor
                HAVING COUNT(f2.produtor) > 1
            )
            AND f.venceu = TRUE
            ORDER BY f.produtor, f.ano
        """;

        Query query = entityManager.createQuery(sql);
        List<Object[]> results = query.getResultList();

        List<FilmeDTO> filmeDTOs = new ArrayList<>();
        for (Object[] row : results) {
            FilmeDTO dto = new FilmeDTO(
                (Integer) row[0],  // ano
                (String) row[1],   // produtor
                (String) row[2]    // nome
            );
            filmeDTOs.add(dto);
        }
        
        return filmeDTOs;
    }

 public ResultadoIntervalosDTO calcularIntervalos(List<FilmeDTO> filmesVencedores) {
     
     // 1. Agrupar Anos de Vitória por Produtor
     // Map<Produtor, Lista de Anos de Vitória>
     Map<String, List<Integer>> produtorAnos = new HashMap<>();
     
     for (FilmeDTO filme : filmesVencedores) {
         produtorAnos
             .computeIfAbsent(filme.getProdutor(), k -> new ArrayList<>())
             .add(filme.getAno());
     }

     // 2. Inicializar variáveis para Min e Max
     List<IntervaloDTO> minIntervalos = new ArrayList<>();
     int menorIntervalo = Integer.MAX_VALUE;

     List<IntervaloDTO> maxIntervalos = new ArrayList<>();
     int maiorIntervalo = Integer.MIN_VALUE;

     // 3. Iterar sobre os produtores com múltiplas vitórias
     for (Map.Entry<String, List<Integer>> entry : produtorAnos.entrySet()) {
         String produtor = entry.getKey();
         List<Integer> anos = entry.getValue();
         
         // Se o produtor só ganhou uma vez, pulamos
         if (anos.size() < 2) continue;

         // Itera sobre a lista de anos para calcular o intervalo
         for (int i = 1; i < anos.size(); i++) {
             int previousWin = anos.get(i - 1);
             int followingWin = anos.get(i);
             int intervaloAtual = followingWin - previousWin;

             IntervaloDTO intervaloDTO = new IntervaloDTO(
                 produtor,
                 intervaloAtual,
                 previousWin,
                 followingWin
             );
             
             // --- Lógica para MÍNIMO ---
             if (intervaloAtual < menorIntervalo) {
                 menorIntervalo = intervaloAtual;
                 minIntervalos.clear(); // Limpa a lista, achamos um NOVO menor
                 minIntervalos.add(intervaloDTO);
             } else if (intervaloAtual == menorIntervalo) {
                 minIntervalos.add(intervaloDTO); // Adiciona, é um empate
             }
             
             // --- Lógica para MÁXIMO ---
             if (intervaloAtual > maiorIntervalo) {
                 maiorIntervalo = intervaloAtual;
                 maxIntervalos.clear(); // Limpa a lista, achamos um NOVO maior
                 maxIntervalos.add(intervaloDTO);
             } else if (intervaloAtual == maiorIntervalo) {
                 maxIntervalos.add(intervaloDTO); // Adiciona, é um empate
             }
         }
     }
     
   
     return new ResultadoIntervalosDTO(minIntervalos, maxIntervalos);
 }

 public ResultadoIntervalosDTO buscarIntervalosProdutores() {
     
    List<FilmeDTO> filmesVencedores = buscarProdutoresComMultiplosPremioss(); // Use seu método atual
     
     return calcularIntervalos(filmesVencedores);
 }
}