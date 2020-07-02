# OrenzLaundry (Mobile Repository)

Halo! Selamat datang di repository web Orenz Laundry, berikut merupakan aplikasi berbasis mobile dan website (pada form admin) yang merupakan sebuah aplikasi yang terintegerasi dengan REST API untuk melakukan transaksi laundry secara online sehingga pelanggan tidak perlu pergi ke tempat laundry, selain itu Orenz Laundry dari sisi website membantu admin dan pemilik laundry untuk melakukan manajemen transaksi, serta pelaporan yang lebih terinci dan lengkap.

## Requirements (Kebutuhan)
- [PHP](https://php.net/) versi 5.6 atau lebih baru.
- [Android Studio](https://developer.android.com/studio) versi 3.6 atau lebih baru
- [XAMPP](https://www.apachefriends.org/download.html) 7.2.28 atau lebih baru.
- [Codeigniter](https://codeigniter.com/en/download) versi 3.1.11
- [Visual Studio Code](https://code.visualstudio.com/download) ( an option for your text editor )


## Instalasi

1. Jalankan tools yang anda perlukan diatas sesuai prosedur masing - masing,
2. Buka Android Studio, pilih Check out project from Version Control, lalu pilih Git
3. Tempel link repository ini ke kolom URL dan pilih direktory penyimpanan project lalu klik Clone.
4. Jika ingin menghubungkan dengan server lokal anda, sesuaikan baris ke 18 pada file `app/java/com.example.kelompok1/Helper/SessionManager.java` menjadi sebagai berikut, (ubah ip sesuai server lokal anda).
``` java
public static final String BASE_URL = "http://192.168.1.1/kelompok1_tif_d/OrenzLaundry/";
```


## Fact & Tips
- Seharusnya repository ini terkoneksi dengan repository web admin Orenz Laundry yang dapat diakses pada link berikut [OrenzLaundry Web](https://github.com/WildanE41181398/kelompok1_tif_d), kami menyarankan untuk melihatnya juga untuk pemahaman yang lebih maksimal,
- Pengerjaan sistem informasi ini dibuat selama 3 bulan mulai dari April 2020 - Juni 2020,
- Repository ini dibangun oleh Mahasiswa Politeknik Negeri Jember, Jurusan Teknologi Informasi dengan tim 6 orang dan menggunakan Scrum dalam pengembangannya.
