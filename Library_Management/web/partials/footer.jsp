<%--
    Footer Partial
    Purpose: Provides a consistent and styled footer across the application.
    Includes: Copyright, social media links (optional), and Bootstrap/custom JS.
--%>
<footer class="bg-dark text-white-50 py-4 mt-auto">
    <div class="container">
        <div class="row">
            <div class="col-md-6 text-center text-md-start mb-3 mb-md-0">
                <p class="mb-0">&copy; 2025 **Library Management System**. All rights reserved.</p>
                <small>Developed by Le Tran Bao Duy and Do Hoang Quoc Phong</small>
            </div>
            <div class="col-md-6 text-center text-md-end">
                <p class="mb-2">Connect with us:</p>
                <a href="#" class="text-white-50 mx-2" aria-label="Facebook">
                    <i class="bi bi-facebook fs-4"></i>
                </a>
                <a href="#" class="text-white-50 mx-2" aria-label="Twitter">
                    <i class="bi bi-twitter fs-4"></i>
                </a>
                <a href="#" class="text-white-50 mx-2" aria-label="LinkedIn">
                    <i class="bi bi-linkedin fs-4"></i>
                </a>
                <a href="#" class="text-white-50 mx-2" aria-label="Instagram">
                    <i class="bi bi-instagram fs-4"></i>
                </a>
            </div>
        </div>
        <hr class="border-secondary mt-3 mb-2">
        <p class="text-center text-white-50 small mb-0">
            Need help? Contact us at <a href="mailto:support@yourlibrary.com" class="text-white-50 text-decoration-none">support@FPTLibrary.com</a>
        </p>
    </div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>

<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>

</body>
</html>