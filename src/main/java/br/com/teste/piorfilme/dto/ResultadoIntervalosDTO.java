package br.com.teste.piorfilme.dto;

import java.util.List;

public class ResultadoIntervalosDTO {
	private List<IntervaloDTO> min;
    private List<IntervaloDTO> max;
    
    public ResultadoIntervalosDTO(List<IntervaloDTO> min, List<IntervaloDTO> max) {
        this.min = min;
        this.max = max;
    }
	public List<IntervaloDTO> getMin() {
		return min;
	}
	public void setMin(List<IntervaloDTO> min) {
		this.min = min;
	}
	public List<IntervaloDTO> getMax() {
		return max;
	}
	public void setMax(List<IntervaloDTO> max) {
		this.max = max;
	}
    
    
}
