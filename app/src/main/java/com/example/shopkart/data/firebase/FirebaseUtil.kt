package com.example.shopkart.data.firebase

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.shopkart.data.model.CartItem
import com.example.shopkart.data.model.Product
import com.example.shopkart.data.model.User
import com.example.shopkart.util.Resource
import com.example.shopkart.util.getExtension
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
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
                onResponse(Resource.Error(it.message)) // failed to retrieve product.
            }
    }

    /**
     * Check product already exists in Cart Items collection.
     */
    fun checkProductAlreadyExist(productId: String, onResponse: (Resource<Boolean>) -> Unit) {
        firebaseAuth.currentUser?.uid?.let { userId ->
            fireStoreDb
                .collection(CART_ITEM_COLLECTION)
                .whereEqualTo(CART_USER_ID,userId)
                .whereEqualTo(CART_PRODUCT_ID,productId)
                .get()
                .addOnSuccessListener {
                    if(it.documents.size > 0){
                        onResponse(Resource.Success(true))
                    } else {
                        onResponse(Resource.Error("",false)) // failed to retrieve product.
                    }

                }
                .addOnFailureListener {
                    onResponse(Resource.Error(it.message,false)) // failed to retrieve product.
                }
        }

    }

    companion object {
        const val USER_COLLECTION = "users"
        const val PRODUCT_COLLECTION = "products"
        const val CART_ITEM_COLLECTION = "cart_items"
        const val PROFILE = "profile_images"
        const val PRODUCTS = "products"
        const val TAG = "FirebaseUtil"
        const val USER_ID = "user_id"
        const val CART_PRODUCT_ID = "productId"
        const val CART_USER_ID = "userId"
    }
}