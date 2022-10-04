package com.example.springproject.ManagedBeans;

import com.example.springproject.Entities.*;
import com.example.springproject.Services.ShoppingService;
import com.example.springproject.Services.UserService;
import com.example.springproject.SessionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Convert;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.transaction.Transactional;
import java.io.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@ManagedBean
@SessionScoped
public class ShoppingBean {

    @ManagedProperty(value = "shoppingService")
    public ShoppingService      shoppingService;

    @ManagedProperty(value = "userService")
    public UserService          userService;


    private List<Category>      categories;
    private List<Product>       products;
    private Category            _category;
    private Product             _product;
    private Basket              _basket;
    private UploadedFile        uploadedFile;
    private Order               order;


    public ShoppingBean(ShoppingService shoppingService, UserService userService) {this.shoppingService = shoppingService;
    this.userService = userService;}


    public Basket get_basket() {return _basket;}
    public void set_basket(Basket _basket) {this._basket = _basket;}
    public Product get_product() {
        return _product;
    }
    public void set_product(Product _product) {
        this._product = _product;
    }
    public Category get_category() {
        return _category;
    }
    public void set_category(Category _category) {
        this._category = _category;
    }
    public List<Category> getCategories() {
        return categories;
    }
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
    public List<Product> getProducts() {
        return products;
    }
    public void setProducts(List<Product> products) {
        this.products = products;
    }
    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }
    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }
    public Order getOrder() {return order;}
    public void setOrder(Order order) {this.order = order;}


    public void onload()
    {
        uploadedFile = null;
        this.categories = shoppingService.getAllCategories();
        this.products = shoppingService.getAllProducts();
        _category = new Category();
    }

    @Transactional
    public void deleteCategory(Category category)
    {
        List<Product> products = shoppingService.getProductsByCategory(category);
        shoppingService.deleteProductsFromBaskets(products);
        shoppingService.deleteProductsByCategory_Id(category.getId());
        shoppingService.deleteCategory(category);
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Success !",
                        "Category Deleted"));
    }

    public String editCategory(Category category)
    {
        this._category = category; return "/edit_category.xhtml?faces-redirect=true";
    }

    public String createCategory()
    {
        categories = shoppingService.getAllCategories();
        this._category = new Category(); return "/create_category.xhtml?faces-redirect=true";
    }

    @Transactional
    public void deleteProduct(Product product)
    {
        shoppingService.deleteProductsFromBaskets(product);
        shoppingService.deleteProduct(product);
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Success !",
                        "Product Deleted"));
    }

    public String editProduct(Product product)
    {
        this._product = product; return "/edit_product.xhtml?faces-redirect=true";
    }

    public String createProduct()
    {
        this._product = new Product();
        return "/create_product.xhtml?faces-redirect=true";
    }

    public String updateCategory()
    {
        uploadCategoryImage(_category);
        shoppingService.updateCategory(this._category); return "/shopping_settings.xhtml?faces-redirect=true";
    }

    public String updateProduct()
    {
        _product.getCategory().setCategory_name(shoppingService.getCategoryById(_product.getCategory().getId()).get().getCategory_name());
        _product.getCategory().setDiscount_percent(shoppingService.getCategoryById(_product.getCategory().getId()).get().getDiscount_percent());
        uploadProductImage(_product);
        productDiscount(_product);
        return "/shopping_settings.xhtml?faces-redirect=true";
    }

    public String save_category(){
        if(categories.stream().anyMatch(u -> u.getCategory_name().equals(_category.getCategory_name())))
        {
            FacesContext.getCurrentInstance().addMessage(
                    "createCategory:category_name",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Category name already in use !",
                            "Category name already in use !"));
            return null;
        }
        _category.setDiscount_percent(0);
        uploadCategoryImage(_category);
        shoppingService.updateCategory(_category); return "/shopping_settings.xhtml?faces-redirect=true";
    }

    public String save_product()
    {
        _category.setCategory_name(shoppingService.getCategoryById(_category.getId()).get().getCategory_name());
        _category.setDiscount_percent(shoppingService.getCategoryById(_category.getId()).get().getDiscount_percent());
        _product.setCategory(_category);
        _product.setDiscount_percent(0);
        _product.setDiscount_price(_product.getPrice() - (_product.getPrice()  * _product.getCategory().getDiscount_percent() / 100) - (_product.getPrice()  * _product.getDiscount_percent() / 100));
        uploadProductImage(_product);
        shoppingService.updateProduct(_product); return "/shopping_settings.xhtml?faces-redirect=true";
    }


    public String addToBasket(Product product)
    {
        HttpSession session = SessionUtils.getSession();
        if(session.getAttribute("user_id") == null)
            return "/login.xhtml?faces-redirect=true";

        int id = (Integer)session.getAttribute("user_id");

            if(product.getBasket_quantity() < 1) {
                product.setBasket_quantity(0);
                return null;
            }
            _basket = shoppingService.getBasketByUserId(id);
            for (Product basket_p: _basket.getProducts()) {
                if(basket_p.getProduct_id() == product.getProduct_id())
                {
                    int qty = basket_p.getBasket_quantity();
                    qty += product.getBasket_quantity();
                    basket_p.setBasket_quantity(qty);
                    shoppingService.updateBasket(_basket);
                    FacesContext.getCurrentInstance().addMessage(
                            null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "Success !",
                                    "Product Added to the Basket"));
                    product.setBasket_quantity(0);
                    return null;
                }
            }
            _basket.getProducts().add(product);
            shoppingService.updateBasket(_basket);
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Success !",
                        "Product Added to The Basket"));
        product.setBasket_quantity(0);
        return null;
    }

    public void basketProducts()
    {
            HttpSession session = SessionUtils.getSession();
            if(session != null && session.getAttribute("user_id") != null)
            {
                int id = (Integer)session.getAttribute("user_id");
                _basket = shoppingService.getBasketByUserId(id);
                products = _basket.getProducts();
                float total_price = 0;
                for (Product product:products)
                {
                    total_price += (product.getDiscount_price() * product.getBasket_quantity());
                }
                _basket.setTotal_price(total_price);
            }
    }

    public void deleteFromBasket(Product product)
    {
        _basket.getProducts().remove(product);
        shoppingService.updateBasket(_basket);
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Success !",
                        "Product Discarded"));
    }

    public String getProductsByCategory(Category category)
    {
        products = shoppingService.getProductsByCategory(category);
        return "/products?faces-redirect=true";
    }

    public String getOrderDetails(Order order)
    {
        this.order = order;
        return "/order_details?faces-redirect=true";
    }

    @Transactional
    public void buy()
    {
        List<Product> basket_products = _basket.getProducts();
        if(basket_products.isEmpty())
        {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Error !",
                            "Your Basket is Empty !"));
            return;
        }
        for (Product b_product: basket_products) {
            if(b_product.getBasket_quantity() > b_product.getProduct_quantity())
            {
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Out of Stock !",
                                b_product.getProduct_name() + " is out of stock ! Current stock = " + b_product.getProduct_quantity()));
                return;
            }
        }

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setOrderProducts(new ArrayList<OrderProduct>());
        float price = _basket.getTotal_price();
        for (Product b_product: basket_products) {
            //orderProduct.getOrders().add(order);
            order.getOrderProducts().add(new OrderProduct(b_product.getProduct_name(), b_product.getDiscount_price(), b_product.getBasket_quantity(), b_product.getImage()));
        }
        order.setPrice(price);
        HttpSession session = SessionUtils.getSession();
        String uname = (String)session.getAttribute("username");
        User user = userService.getUserByUname(uname);
        user.getOrders().add(order);
        userService.updateUser(user);

        for (Product b_product:basket_products)
        {
            Product repo_product = shoppingService.getProductById(b_product.getProduct_id());
            repo_product.setProduct_quantity(repo_product.getProduct_quantity() - b_product.getBasket_quantity());
            shoppingService.updateProduct(repo_product);
        }
        _basket.getProducts().removeAll(_basket.getProducts());
        shoppingService.updateBasket(_basket);
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Success !",
                        "Purchase Successful"));
    }

    public void categoryDiscount(Category category)
    {
        if(category.getDiscount_percent() < 0 || category.getDiscount_percent() > 99)
            return;
        List<Product> products = shoppingService.getProductsByCategory(category);
        for (Product product: products) {
            float price = product.getPrice();
            product.setDiscount_price(price - (price * category.getDiscount_percent() / 100) - (price * product.getDiscount_percent() / 100));
        }
        shoppingService.updateAllProducts(products);
        shoppingService.updateCategory(category);
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Success !",
                        "Discount Has Been Made"));
    }

    public void productDiscount(Product product)
    {
        if(product.getDiscount_percent() < 0 || product.getDiscount_percent() > 99)
            return;
        float price = product.getPrice();
        product.setDiscount_price(price - (price * product.getCategory().getDiscount_percent() / 100) - (price * product.getDiscount_percent() / 100));
        shoppingService.updateProduct(product);
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Success !",
                        "Discount Has Been Made"));

    }

    private String encodeFileToBase64Binary(File file){
        String encodedfile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = new String(Base64.encodeBase64(bytes), "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encodedfile;
    }

    public String defCategoryImage()
    {
        File f =  new File("src/main/webapp/resources/img/category_image.jpg");
        return encodeFileToBase64Binary(f);
    }

    public String defProductImage()
    {
        File f = new File("src/main/webapp/resources/img/product_image.jpg");
        return encodeFileToBase64Binary(f);
    }

    public void uploadCategoryImage(Category category){
        if(uploadedFile == null)
            return;
        byte[] img = uploadedFile.getContent();
        category.setImage(Base64.encodeBase64String(img));
        uploadedFile = null;
    }

    public void resetCategoryImage(Category category)
    {
        category.setImage(null);
        shoppingService.updateCategory(category);
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Success !",
                        "Category Image Has Been Set to Default"));
    }

    public void uploadProductImage(Product product)
    {
        if(uploadedFile == null)
            return;
        byte[] img = uploadedFile.getContent();
        product.setImage(Base64.encodeBase64String(img));
        uploadedFile = null;
    }

    public void resetProductImage(Product product)
    {
        product.setImage(null);
        shoppingService.updateProduct(product);
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Success !",
                            "Product Image Has Been Set to Default"));
    }

}
