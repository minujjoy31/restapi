package com.hackerrank.sample.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.hackerrank.sample.dto.FilteredProducts;
import com.hackerrank.sample.dto.SortedProducts;

@RestController
public class SampleController {

	
	   final String uri = "https://jsonmock.hackerrank.com/api/inventory";
	   RestTemplate restTemplate = new RestTemplate();
	   String result = restTemplate.getForObject(uri, String.class);			
	   JSONObject root = new JSONObject(result);
	   
	   JSONArray data = root.getJSONArray("data");
	   
	   
		
		@CrossOrigin
		@GetMapping("/filter/price/{initial_price}/{final_price}")  
		private ResponseEntity< ArrayList<FilteredProducts> > filtered_books(@PathVariable("initial_price") int init_price , @PathVariable("final_price") int final_price)   
		{
            List<JSONObject> jsonObjects = new ArrayList<>();
            ArrayList<FilteredProducts> books = new ArrayList<FilteredProducts>();
			try {
                if (data!= null) {
                    for (int i = 0; i < data.length(); i++) {
                        jsonObjects.add(data.getJSONObject(i));
                    }


                }
                jsonObjects.stream()
                        .filter(obj -> obj.getInt("price") > init_price &&  obj.getInt("price") < final_price )
                        .forEach(o-> books.add(new FilteredProducts(o.getString("barcode"))));

                if (books.isEmpty()){
                    return new ResponseEntity<ArrayList<FilteredProducts>>(books, HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<ArrayList<FilteredProducts>>(books, HttpStatus.OK);

			   
			    
			}
            catch(Exception E)
				{
	   	System.out.println("Error encountered : "+E.getMessage());
	    return new ResponseEntity<ArrayList<FilteredProducts>>(HttpStatus.NOT_FOUND);
				}
			
		}  
		
		
		@CrossOrigin
		@GetMapping("/sort/price")  
		private ResponseEntity<SortedProducts[]> sorted_books()   
		{
            List<JSONObject> jsonObjects = new ArrayList<>();
			try {
                if (data!= null) {
                    for (int i = 0; i < data.length(); i++) {
                        jsonObjects.add(data.getJSONObject(i));
                    }


                }//
                SortedProducts[] ans=new SortedProducts[data.length()];
                jsonObjects = jsonObjects.stream()
                        .sorted(Comparator.comparing(o->o.getInt("price")))
                        .collect(Collectors.toList());
                List<JSONObject> finalJsonObjects = jsonObjects;
                IntStream.range(0, jsonObjects.size())
                        .forEach(i -> {
                            ans[i]=new SortedProducts(finalJsonObjects.get(i).getString("barcode"));
                        });
			    return new ResponseEntity<SortedProducts[]>(ans, HttpStatus.OK);
			    
			}catch(Exception E)
				{
	   	System.out.println("Error encountered : "+E.getMessage());
	    return new ResponseEntity<SortedProducts[]>(HttpStatus.NOT_FOUND);
				}
			
		}  
		
		
	
}
