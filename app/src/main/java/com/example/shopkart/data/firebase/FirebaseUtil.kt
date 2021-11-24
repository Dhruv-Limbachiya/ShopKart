package com.example.shopkart.data.firebase

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.shopkart.data.model.*
import com.example.shopkart.util.Resource
import com.example.shopkart.util.getExtension
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.WriteBatch
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlin.random.Random

/**
 * Created By Dhruv Limbachiya on 14-10-2021 11:38.
 */

class FirebaseUtil {

    // Get the FirebaseAuth instance for authentication like login, sign up, forgot password & sign out.
    val firebaseAuth = FirebaseAuth.getInstance()

    // Get the Firebase Firestore instance for storing & retrieving data.
    private val fireStoreDb = Firebase.firestore

    // Get the Firebase Storage reference.
    private val cloudStorage = FirebaseStorage.getInstance().reference

    /**
     * Method to save the registered user info into Cloud Firestore.
     */
    fun saveUser(user: User, onResponse: (Resource<String>) -> Unit) {
        fireStoreDb.collection(USER_COLLECTION)
            .document(user.uid ?: Random.toString())
            .set(
                user,
                SetOptions.merge()
            ) // set user data in the given document, if it is already existed then merge the new data with the existing one.
            .addOnSuccessListener {
                onResponse(Resource.Success("Registration Successful"))
                Log.i(TAG, "saveUser: Data saved")
            }
            .addOnFailureListener {
                onResponse(Resource.Error(it.message.toString()))
                Log.i(TAG, "saveUser: ${it.message.toString()}")
            }
    }

    /**
     * Method to get the user details from the "users" collection.
     */
    fun getUserDetails(user: (User) -> Unit) {
        firebaseAuth.currentUser?.uid?.let { uid ->
            fireStoreDb
                .collection(USER_COLLECTION)
                .document(uid)
                .get()
                .addOnSuccessListener { snapshot ->
                    snapshot.toObject(User::class.java)?.let {
                        user(it)
                    }
                }
                .addOnFailureListener {
                    Log.e(TAG, it.message.toString())
                }
        }
    }


    /**
     * Updates the user details on FireStore.
     */
    fun updateUserProfile(
        userHashMap: HashMap<String, Any>,
        onResponse: (Resource<String>) -> Unit
    ) {
        onResponse(Resource.Loading())
        firebaseAuth.currentUser?.uid?.let { uid ->
            fireStoreDb
                .collection(USER_COLLECTION)
                .document(uid)
                .update(userHashMap)
                .addOnSuccessListener {
                    onResponse(Resource.Success("Profile updated!"))
                }
                .addOnFailureListener {
                    onResponse(Resource.Error(it.message.toString()))
                    Log.i(TAG, "updateUserProfile: ${it.message.toString()}")
                }
        }
    }

    /**
     * Upload profile image on Firebase cloud storage.
     */
    fun uploadImageToCloudStorage(
        context: Context,
        imageUri: Uri,
        rootFolderName: String,
        onResponse: (Resource<String>) -> Unit
    ) {
        onResponse(Resource.Loading())
        cloudStorage
            .child(
                "$rootFolderName/${System.currentTimeMillis()}.${
                    getExtension(
                        imageUri,
                        context
                    )
                }"
            ) // image path.
            .putFile(imageUri) // uri to upload
            .addOnSuccessListener {
                it.metadata?.reference?.downloadUrl?.addOnSuccessListener { url -> // download the uploaded image url.
                    onResponse(Resource.Success(data = url.toString()))
                }
            }
            .addOnFailureListener {
                onResponse(Resource.Error(it.message))
            }
    }

    /**
     * Uploads the product details on firestore db.
     */
    fun uploadProductDetailsOnFirestore(product: Product, onResponse: (Resource<String>) -> Unit) {
        fireStoreDb
            .collection(PRODUCT_COLLECTION)
            .document()
            .set(product, SetOptions.merge())
            .addOnSuccessListener {
                onResponse(Resource.Success("Product uploaded successfully."))
            }
            .addOnFailureListener {
                onResponse(Resource.Error(it.message.toString()))
                Log.i(TAG, "uploadProductDetailsOnFirestore: ${it.message.toString()}")
            }
    }

    /**
     * Fetches products which are added by the current logged in user.
     */
    fun getProductsFromFireStore(onResponse: (Resource<Any>) -> Unit) {
        firebaseAuth.currentUser?.uid?.let {
            onResponse(Resource.Loading())
            fireStoreDb.collection(PRODUCT_COLLECTION)
                .whereEqualTo(
                    USER_ID,
                    it
                ) // Get all the products which are added by current logged in user.
                .get()
                .addOnSuccessListener {
                    val documents = it.documents // Retrieve all the documents.
                    val products = ArrayList<Product>()
                    for (doc in documents) {
                        val product = doc.toObject(Product::class.java)
                        if (product != null) {
                            product.id = doc.id // Assigns doc id as product id in product object.
                            products.add(product)
                        }
                    }

                    onResponse(Resource.Success(products)) // indicates the products received successfully.
                }
                .addOnFailureListener {
                    onResponse(Resource.Error(it.message)) // failed to retrieve products.
                }
        }
    }

    /**
     * Fetches all the products.
     */
    fun getAllProductsFromFireStore(onResponse: (Resource<Any>) -> Unit) {
        onResponse(Resource.Loading())
        fireStoreDb.collection(PRODUCT_COLLECTION)
            .get()
            .addOnSuccessListener {
                val documents = it.documents // Retrieve all the documents.
                val products = ArrayList<Product>()
                for (doc in documents) {
                    val product = doc.toObject(Product::class.java)
                    if (product != null) {
                        product.id = doc.id // Assigns doc id as product id in product object.
                        products.add(product)
                    }
                }

                onResponse(Resource.Success(products)) // indicates the products received successfully.
            }
            .addOnFailureListener {
                onResponse(Resource.Error(it.message)) // failed to retrieve products.
            }
    }

    /**
     * Delete product on Firestore db.
     */
    fun deleteProduct(productId: String, onResponse: (Resource<String>) -> Unit) {
        onResponse(Resource.Loading())
        fireStoreDb
            .collection(PRODUCT_COLLECTION)
            .document(productId)
            .delete()
            .addOnSuccessListener {
                onResponse(Resource.Success("Product deleted successfully.")) // indicates the products received successfully.
            }
            .addOnFailureListener {
                onResponse(Resource.Error(it.message)) // failed to retrieve products.
            }
    }

    /**
     * Get the product details by its product id.
     */
    fun getProductDetailsById(productId: String, onResponse: (Resource<Any>) -> Unit) {
        onResponse(Resource.Loading())
        fireStoreDb
            .collection(PRODUCT_COLLECTION)
            .document(productId)
            .get()
            .addOnSuccessListener {
                it.toObject(Product::class.java)?.let { product ->
                    onResponse(Resource.Success(product)) // Product data received successfully.
                }
            }
            .addOnFailureListener {
                onResponse(Resource.Error(it.message)) // failed to retrieve product.
            }
    }

    /**
     * Upload cart item details on Firestore db.
     */
    fun uploadCartItem(cartItem: CartItem, onResponse: (Resource<String>) -> Unit) {
        onResponse(Resource.Loading())
        fireStoreDb
            .collection(CART_ITEM_COLLECTION)
            .document()
            .set(cartItem, SetOptions.merge())
            .addOnSuccessListener {
                onResponse(Resource.Success("Product added to cart."))
            }
            .addOnFailureListener {
                onResponse(Resource.Error(it.message))
            }
    }

    /**
     * Check product already exists in Cart Items collection.
     */
    fun checkProductAlreadyExist(productId: String, onResponse: (Boolean) -> Unit) {
        firebaseAuth.currentUser?.uid?.let { userId ->
            fireStoreDb
                .collection(CART_ITEM_COLLECTION)
                .whereEqualTo(CART_USER_ID, userId)
                .whereEqualTo(CART_PRODUCT_ID, productId)
                .get()
                .addOnSuccessListener {
                    if (it.documents.size > 0) {
                        onResponse(true)
                    } else {
                        onResponse(false) // failed to retrieve product.
                    }

                }
                .addOnFailureListener {
                    onResponse(false) // failed to retrieve product.
                }
        }
    }

    /**
     * Get user cart items.
     */
    fun getCartItems(onResponse: (Resource<List<CartItem>>) -> Unit) {
        firebaseAuth.currentUser?.uid?.let { userId ->
            onResponse(Resource.Loading())
            fireStoreDb
                .collection(CART_ITEM_COLLECTION)
                .whereEqualTo(CART_USER_ID, userId)
                .get()
                .addOnSuccessListener {
                    if (it.documents.size > 0) {
                        val cartItems = mutableListOf<CartItem>()

                        for (doc in it.documents) {
                            doc.toObject(CartItem::class.java)?.let { item ->
                                item.id = doc.id // Add doc id as cart item id.
                                cartItems.add(item)
                            }
                        }

                        onResponse(Resource.Success(cartItems))
                    } else {
                        onResponse(Resource.Error("No cart item found!")) // failed to retrieve product.
                    }

                }
                .addOnFailureListener {
                    onResponse(Resource.Error(it.message)) // failed to retrieve cart items.
                }
        }
    }

    /**
     * Removes the specified cart item from the "cart_items" collection on FireStore db.
     */
    fun removeCartItemOnFireStoreDb(cartItemId: String, onResponse: (Resource<String>) -> Unit) {
        fireStoreDb.collection(CART_ITEM_COLLECTION)
            .document(cartItemId)
            .delete()
            .addOnSuccessListener {
                onResponse(Resource.Success("Item removed from the cart."))
            }
            .addOnFailureListener {
                onResponse(Resource.Error(it.message.toString()))
            }
    }


    /**
     * Updates the cart item data.
     */
    fun updateCart(
        cartItemId: String,
        data: HashMap<String, Any>,
        onResponse: (Resource<String>) -> Unit
    ) {
        fireStoreDb.collection(CART_ITEM_COLLECTION)
            .document(cartItemId)
            .update(data)
            .addOnSuccessListener {
                onResponse(Resource.Success("Cart updated."))
            }
            .addOnFailureListener {
                onResponse(Resource.Error(it.message.toString()))
            }
    }

    /**
     * Upload address details on Firestore db.
     */
    fun uploadAddressDetails(address: Address, onResponse: (Resource<String>) -> Unit) {
        onResponse(Resource.Loading())
        fireStoreDb
            .collection(ADDRESS_COLLECTION)
            .document()
            .set(address, SetOptions.merge())
            .addOnSuccessListener {
                onResponse(Resource.Success("Address saved successfully"))
            }
            .addOnFailureListener {
                onResponse(Resource.Error(it.message))
            }
    }

    /**
     * Gets the currently logged in user addresses from the Firestore db.
     */
    fun getMyAddressesFromFireStore(onResponse: (Resource<List<Address>>) -> Unit) {
        firebaseAuth.currentUser?.uid?.let { uid ->
            onResponse(Resource.Loading())
            fireStoreDb.collection(ADDRESS_COLLECTION)
                .whereEqualTo(USER_ID, uid)
                .get()
                .addOnSuccessListener {
                    val documents = it.documents

                    if (documents.size > 0) {
                        val addresses = mutableListOf<Address>()
                        for (doc in documents) {
                            // Convert each non-nullable doc into Address Object and assign doc id as address id.
                            doc.toObject(Address::class.java)?.let { address ->
                                address.id = doc.id
                                addresses.add(address) // Add address to addresses list.
                            }

                        }
                        Log.i(TAG, "getMyAddressesFromFireStore: : $addresses")
                        onResponse(Resource.Success(addresses))
                    } else {
                        onResponse(Resource.Error("Address not found!"))
                    }
                }
                .addOnFailureListener {
                    onResponse(Resource.Error(it.message))
                }

        }
    }

    /**
     * Delete the specified address from the "addresses" collection on FireStore db.
     */
    fun deleteAddressOnFireStoreDb(addressId: String, onResponse: (Resource<String>) -> Unit) {
        fireStoreDb.collection(ADDRESS_COLLECTION)
            .document(addressId)
            .delete()
            .addOnSuccessListener {
                onResponse(Resource.Success("Address deleted!"))
            }
            .addOnFailureListener {
                onResponse(Resource.Error(it.message.toString()))
            }
    }

    /**
     * Update address info on Firestore Db.
     */
    fun updateAddressDetailOnFireStore(address: Address, onResponse: (Resource<String>) -> Unit) {
        fireStoreDb.collection(ADDRESS_COLLECTION)
            .document(address.id)
            .set(address, SetOptions.merge())
            .addOnSuccessListener {
                onResponse(Resource.Success("Address updated!"))
            }
            .addOnFailureListener {
                onResponse(Resource.Error(it.message.toString()))
            }
    }

    /**
     * Uploads the order details on Firestore cloud.
     */
    fun uploadOrderDetails(order: Order, onResponse: (Resource<String>) -> Unit) {
        onResponse(Resource.Loading())
        fireStoreDb
            .collection(ORDER_COLLECTION)
            .document()
            .set(order, SetOptions.merge())
            .addOnSuccessListener {
                updateProductAndCartDetails(order) {
                    onResponse(it)
                }
            }
            .addOnFailureListener {
                onResponse(Resource.Error(it.message))
            }
    }

    /**
     * Method will the stock quantity of product and remove all cart item after placing a successful order.
     */
    private fun updateProductAndCartDetails(
        order: Order,
        onResponse: (Resource<String>) -> Unit
    ) {
        val writeBatch = fireStoreDb.batch()

        for (cartItem in order.items) {
            val dataHashMap = hashMapOf<String, Any>()

            // updated value of stock quantity after checkout.
            dataHashMap[STOCK_QUANTITY] =
                (cartItem.stock_quantity.toInt() - cartItem.cart_quantity.toInt()).toString() // Subtract the cart quantity with the product total stock quantity.

            val productsReference = fireStoreDb
                .collection(PRODUCT_COLLECTION)
                .document(cartItem.productId) // Reference an individual product in products collection.

            writeBatch.update(
                productsReference,
                dataHashMap
            ) // updates the stock quantity of product.

            addSoldProductInFirestore(cartItem, order, writeBatch)
        }

        for (cartItem in order.items) {
            val cartItemsReference =
                fireStoreDb.collection(CART_ITEM_COLLECTION).document(cartItem.id)
            writeBatch.delete(cartItemsReference) // Deletes the cart item after placing an order.
        }

        writeBatch.commit()
            .addOnSuccessListener {
                onResponse(Resource.Success("Your order placed successfully"))
            }.addOnFailureListener {
                onResponse(Resource.Error("Failed to update product and cart details."))
            }
    }

    /**
     * Add the sold product details in Firestore db.
     */
    private fun addSoldProductInFirestore(
        cartItem: CartItem,
        order: Order,
        writeBatch: WriteBatch
    ) {
        val soldProduct = SoldProduct(
            cartItem.productOwnerId,
            cartItem.title,
            cartItem.price,
            cartItem.cart_quantity,
            cartItem.image,
            order.title,
            order.orderDateTime,
            order.sub_total_amount,
            order.shipping_charge,
            order.total_amount,
            order.address,
        )

        val soldProductReference = fireStoreDb
            .collection(SOLD_PRODUCT_COLLECTION)
            .document(cartItem.productId)

        writeBatch.set(soldProductReference, soldProduct)
    }


    /**
     * Get the list of order placed by the current user.
     */
    fun getMyOrders(onResponse: (Resource<List<Order>>) -> Unit) {
        firebaseAuth.currentUser?.uid?.let { userId ->
            onResponse(Resource.Loading())
            fireStoreDb
                .collection(ORDER_COLLECTION)
                .whereEqualTo(ORDER_USER_ID, userId)
                .get()
                .addOnSuccessListener {
                    if (it.documents.size > 0) {
                        val orders = mutableListOf<Order>()

                        for (doc in it.documents) {
                            doc.toObject(Order::class.java)?.let { item ->
                                item.id = doc.id // Add doc id as order item id.
                                orders.add(item)
                            }
                        }

                        onResponse(Resource.Success(orders))
                    } else {
                        onResponse(Resource.Error("It seems like you haven't place any order yet!")) // failed to retrieve order for the current user.
                    }

                }
                .addOnFailureListener {
                    onResponse(Resource.Error(it.message)) //
                }
        }
    }

    /**
     * Get the list of sold product.
     */
    fun getMySoldProducts(onResponse: (Resource<List<SoldProduct>>) -> Unit) {
        firebaseAuth.currentUser?.uid?.let { userId ->
            onResponse(Resource.Loading())
            fireStoreDb
                .collection(SOLD_PRODUCT_COLLECTION)
                .whereEqualTo(SOLD_PRODUCT_USER_ID, userId)
                .get()
                .addOnSuccessListener {
                    if (it.documents.size > 0) {
                        val products = mutableListOf<SoldProduct>()

                        for (doc in it.documents) {
                            doc.toObject(SoldProduct::class.java)?.let { item ->
                                item.id = doc.id // Add doc id as order item id.
                                products.add(item)
                            }
                        }

                        onResponse(Resource.Success(products))
                    } else {
                        onResponse(Resource.Error("It seems like you haven't sold any product yet!")) // failed to retrieve sold product.
                    }

                }
                .addOnFailureListener {
                    onResponse(Resource.Error(it.message))
                }
        }
    }

    companion object {
        const val USER_COLLECTION = "users"
        const val PROFILE = "profile_images"
        const val TAG = "FirebaseUtil"
        const val USER_ID = "user_id"

        // Product collection and its fields
        const val PRODUCTS = "products"
        const val PRODUCT_COLLECTION = "products"
        const val STOCK_QUANTITY = "stock_quantity"

        // Cart collection and its fields
        const val CART_ITEM_COLLECTION = "cart_items"
        const val CART_PRODUCT_ID = "productId"
        const val CART_USER_ID = "userId"
        const val CART_ITEM_QUANTITY = "cart_quantity"

        // Address Collection
        const val ADDRESS_COLLECTION = "addresses"

        // Order Collection
        const val ORDER_COLLECTION = "orders"
        const val ORDER_USER_ID = "user_id"

        // Sold Product Collection
        const val SOLD_PRODUCT_COLLECTION = "sold_products"
        const val SOLD_PRODUCT_USER_ID = "user_id"
    }
}