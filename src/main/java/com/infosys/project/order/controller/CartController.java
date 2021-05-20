package com.infosys.project.order.controller;

import java.util.List;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.infosys.project.order.dto.CartDTO;

@RestController
@Transactional
public class CartController {
	@Value("${cart.uri}")
	String cartUri;
	
//	@Autowired
//	RestTemplate restTemplate;

	@SuppressWarnings("unchecked")
	@GetMapping(value = "/order/cart", produces = MediaType.APPLICATION_JSON_VALUE)
	
	public List<CartDTO> getCartDetails() {
		RestTemplate restTemplate = new RestTemplate();
		List<CartDTO> cartDTOs = restTemplate.getForObject(cartUri, List.class);
		return cartDTOs;
	}

	@PostMapping(value = "/order/cart/add", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addToCart(@RequestBody CartDTO cartDTO) {
		
	RestTemplate restTemplate = new RestTemplate();
		String message = restTemplate.postForObject(cartUri + "/add", cartDTO, String.class);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	@DeleteMapping(value = "/order/cart/{buyerId}/{prodId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> removeFromCart(@PathVariable Long buyerId, @PathVariable Long prodId) {
		
	RestTemplate restTemplate = new RestTemplate();
		CartDTO cartDTO = restTemplate.getForObject(cartUri+"/"+buyerId+"/"+prodId, CartDTO.class);
		if (cartDTO==null)
			return new ResponseEntity<>("Product not present in cart", HttpStatus.OK);
		restTemplate.delete(cartUri + "/remove/"+buyerId+"/"+prodId);
		return new ResponseEntity<>("Product deleted",HttpStatus.OK);

	}
}
