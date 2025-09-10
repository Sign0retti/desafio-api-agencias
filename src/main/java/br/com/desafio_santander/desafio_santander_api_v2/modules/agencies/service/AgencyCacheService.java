package br.com.desafio_santander.desafio_santander_api_v2.modules.agencies.service;

import br.com.desafio_santander.desafio_santander_api_v2.modules.agencies.DTO.AgencyCalculatedDTO;
import br.com.desafio_santander.desafio_santander_api_v2.modules.agencies.DTO.AgencyDTO;
import br.com.desafio_santander.desafio_santander_api_v2.modules.agencies.useCases.AgencyUseCase;
import br.com.desafio_santander.desafio_santander_api_v2.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@EnableScheduling
public class AgencyCacheService {

    private static final Logger logger = LoggerFactory.getLogger(AgencyCacheService.class);
    private static final String CACHE_NAME = "agencies";
    
    @Autowired
    private AgencyUseCase agencyUseCase;
    
    @Autowired
    private CacheManager cacheManager;
    
    // Controla quando foi feita a última consulta GET
    private volatile LocalDateTime lastSearchTime = null;
    
    // Cache para controlar invalidação após 5 minutos da consulta
    private final ConcurrentHashMap<String, LocalDateTime> searchTimestamps = new ConcurrentHashMap<>();

    @Cacheable(value = CACHE_NAME, key = "'allAgencies'")
    public List<AgencyDTO> getAllAgencies() {
        logger.info("Buscando todas as agências do banco de dados (cache miss)");
        return agencyUseCase.readAll();
    }

    public List<AgencyCalculatedDTO> searchAgencies(Double posX, Double posY) {
        String searchKey = posX + "," + posY;
        logger.info("Executando busca de agências para posição: {}, {}", posX, posY);
        
        // Registra o tempo da consulta
        lastSearchTime = LocalDateTime.now();
        searchTimestamps.put(searchKey, lastSearchTime);
        
        // Agenda invalidação após 5 minutos
        scheduleInvalidationAfterSearch();
        
        List<AgencyDTO> agencies = getAllAgencies();
        List<AgencyCalculatedDTO> calculatedList = new ArrayList<>();

        for (AgencyDTO agency : agencies) {
            double distance = Utils.calcDistance(posX, posY, agency.getPosX(), agency.getPosY());
            AgencyCalculatedDTO calculatedDTO = new AgencyCalculatedDTO(agency, distance);
            calculatedList.add(calculatedDTO);
        }

        calculatedList.sort(Comparator.comparingDouble(AgencyCalculatedDTO::getDistance));
        
        logger.info("Busca concluída. {} agências encontradas", calculatedList.size());
        return calculatedList;
    }

    // Invalidação automática a cada 10 minutos
    @Scheduled(fixedRate = 600000) // 10 minutos = 600.000 ms
    public void invalidateCacheEvery10Minutes() {
        logger.info("Invalidando cache automaticamente (renovação de 10 minutos)");
        clearCache();
    }

    // Invalidação 5 minutos após consulta GET
    private void scheduleInvalidationAfterSearch() {
        // Agenda para executar daqui a 5 minutos
        new Thread(() -> {
            try {
                Thread.sleep(300000); // 5 minutos = 300.000 ms
                
                if (lastSearchTime != null && 
                    lastSearchTime.plusMinutes(5).isBefore(LocalDateTime.now().plusSeconds(1))) {
                    logger.info("Invalidando cache 5 minutos após última consulta GET");
                    clearCache();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn("Thread de invalidação foi interrompida", e);
            }
        }).start();
    }

    // Invalida o cache quando uma nova agência é cadastrada
    public void invalidateCacheOnNewAgency() {
        logger.info("Invalidando cache devido ao cadastro de nova agência");
        clearCache();
    }

    private void clearCache() {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            cache.clear();
            logger.info("Cache de agências limpo com sucesso");
        }
        searchTimestamps.clear();
    }
}
