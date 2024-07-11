package com.valoores.v21.usm.app.logs.logsbyheatmap.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.valoores.v21.usm.app.logs.logsbyheatmap.model.LogsHeatMap;

@Repository
public interface ILogsByHeatmapRepository extends PagingAndSortingRepository<LogsHeatMap, Long> {

}
