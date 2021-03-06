package com.michaeldowden.jwf.web;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import com.google.gson.Gson;
import com.michaeldowden.jwf.model.OrderItem;
import com.michaeldowden.jwf.service.CartService;
import com.michaeldowden.jwf.service.ItemDao;

public class CartController {
	private final Gson gson = new Gson();
	private final ItemDao itemDao = ItemDao.getInstance();
	private final CartService cartSvc = new CartService();

	public void initialize() {
		get("/svc/cart", "application/json", (req, res) -> {
			return cartSvc.fetchCart(req);
		}, gson::toJson);

		put("/svc/cart/:itemId", (req, res) -> {
			Integer itemId = Integer.valueOf(req.params(":itemId"));
			final OrderItem item = new OrderItem(itemDao.findBourbon(itemId));

			cartSvc.addToCart(req, item);
			return item;
		});

		post("/svc/cart/:itemId", (req, res) -> {
			Integer itemId = Integer.valueOf(req.params(":itemId"));
			Integer qty = Integer.valueOf(req.queryParams("qty"));

			cartSvc.updateQuantity(req, itemId, qty);
			return itemId;
		});

		delete("/svc/cart/:itemId", (req, res) -> {
			Integer itemId = Integer.valueOf(req.params(":itemId"));

			cartSvc.removeFromCart(req, itemId);
			return itemId;
		});
	}
}
