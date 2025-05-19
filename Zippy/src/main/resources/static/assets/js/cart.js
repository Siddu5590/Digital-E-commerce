function addToCart(productId) {
  fetch(`/add-to-cart/${productId}`, { method: 'POST' })
    .then(res => res.text())
    .then(count => {
      document.getElementById('cart-count').textContent = count;

      // Replace button with quantity selector
      document.getElementById(`button-container-${productId}`).innerHTML = `
        <div class="input-group justify-content-center align-items-center">
          <button class="btn btn-outline-secondary" onclick="updateQty(${productId}, 'decrease')">-</button>
          <span class="px-2 fw-bold" id="qty-${productId}">1</span>
          <button class="btn btn-outline-secondary" onclick="updateQty(${productId}, 'increase')">+</button>
        </div>
      `;
      showCartAlert();
    });
}

function updateQty(productId, action) {
    fetch(`/cart/${action}/${productId}`, {
        method: 'POST'
    })
    .then(res => res.text())
    .then(qty => {
        if (qty === "max") {
            alert("Maximum quantity allowed is 10");
            return;
        }

        const qtySpan = document.getElementById(`qty-${productId}`);
        if (qtySpan) {
            qtySpan.textContent = qty; // Update the quantity displayed on the page
        }

        // Dynamically update total price for the item
        const itemTotal = document.getElementById(`item-total-${productId}`);
        const price = document.getElementById(`price-${productId}`).textContent.replace('₹', '');
        const total = parseFloat(price) * parseInt(qty); // Calculate total for this item
        if (itemTotal) {
            itemTotal.textContent = `₹${total.toFixed(2)}`; // Update the total price for the item
        }

        // Update cart count
        updateCartCount();

        // Optionally update cart total
        updateTotalPrice();
    })
    .catch(err => {
        console.error("Error updating quantity", err);
    });
}

function updateCartCount() {
    fetch("/cart/count")
        .then(res => res.text())
        .then(count => {
            const cartCountSpan = document.getElementById("cart-count");
            if (cartCountSpan) {
                cartCountSpan.textContent = count; // Update the cart count in the header
            }
        });
}

function updateTotalPrice() {
    fetch('/cart')  // Get the updated cart details
        .then(res => res.json())
        .then(cart => {
            let totalPrice = 0;
            cart.cartItems.forEach(item => {
                totalPrice += item.totalPrice;
            });
            document.getElementById("cart-total").textContent = "₹" + totalPrice.toFixed(2); // Update the total price
        })
        .catch(err => {
            console.error("Error updating total price", err);
        });
}

function showCartAlert() {
  const alertBox = document.getElementById("cart-success-alert");
  alertBox.classList.remove("d-none");
  setTimeout(() => {
    alertBox.classList.add("d-none");
  }, 2000);
}

window.onload = updateCartCount;


