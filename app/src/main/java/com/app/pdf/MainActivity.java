package com.app.pdf;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
public class MainActivity extends AppCompatActivity {

    Button btnPDF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPDF = findViewById(R.id.btnPDF);
        btnPDF.setOnClickListener(v -> pdf());

    }

    private void pdf() {
        Document documento =  new Document(PageSize.A4, 20, 20, 0, 20);
        Font FONT_TITLE = new Font(Font.FontFamily.HELVETICA, 30, Font.BOLD);
        Font FONT_FOOTER = new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL);

        try {
            ImageView iv = (ImageView) findViewById(R.id.imageView);

            File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "prueba.pdf");
            //PdfWriter.getInstance(documento, new FileOutputStream(pdfFile));

            //
            ParcelFileDescriptor fb = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY);

            PdfiumCore pdfiumCore = new PdfiumCore(this);

            int pageNum = 0;

            PdfDocument pdfDocument = pdfiumCore.newDocument(fb);

            pdfiumCore.openPage(pdfDocument, pageNum);

            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNum);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNum);

            // ARGB_8888 - best quality, high memory usage, higher possibility of OutOfMemoryError
            // RGB_565 - little worse quality, twice less memory usage
            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.RGB_565);
            pdfiumCore.renderPageBitmap(pdfDocument, bitmap, pageNum, 0, 0,
                    width, height);
            //if you need to render annotations and form fields, you can use
            //the same method above adding 'true' as last param

            iv.setImageBitmap(bitmap);

            printInfo(pdfiumCore, pdfDocument);

            pdfiumCore.closeDocument(pdfDocument); // important!



/*
            documento.open();
            documento.add(createTableImage(30, R.mipmap.ic_google_foreground, 0, 0));
            documento.add(createTable(FONT_TITLE, "LS24R350FZLXZX", 0, 50));
            documento.add(createTable(FONT_FOOTER, "LS24R350FZLXZX", 10, 0));
            documento.add(createTable(FONT_FOOTER, "B09PRSV4MR", 10, 10));
            documento.add(createTableImage(55, R.mipmap.ic_qr_foreground, 0, 0));
            documento.add(createTable(FONT_FOOTER, "QR", 10, 0));
            documento.add(createTable(FONT_FOOTER, "https://www.amazon.com.mx/SAMSUNG-LS24R350FZLXZX-Monitor-Experiencia-inmersiva/dp/B09PRSV4MR?ref_=Oct_DLandingS_D_7c6e4843_0", 10, 0));

            documento.close();
            */
            Toast.makeText(this, "Creado", Toast.LENGTH_SHORT).show();

        }catch(Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }




    }
/*
    private PdfPTable createTableImage( int width, int source, int top, int bottom)  {
        PdfPTable table = new PdfPTable(1);

        Image image = bitmapToImage(source);

        table.setWidthPercentage(width);
        table.getDefaultCell().setBorder(0);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setPaddingBottom(bottom);
        table.getDefaultCell().setPaddingTop(top);

        table.addCell(image);
        return table;
    }

    private PdfPTable createTable(Font font, String text, int top, int bottom) {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(0);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setPadding(5);
        table.getDefaultCell().setPaddingBottom(bottom);
        table.getDefaultCell().setPaddingTop(top);

        table.addCell(new Paragraph(text, font));
        return table;
    }

    private Image bitmapToImage(int source){
        Image img = null;
        try {

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),source);
            ByteArrayOutputStream  stream = new ByteArrayOutputStream();
            Bitmap.createScaledBitmap(bitmap, 10, 10, false);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            img = Image.getInstance(byteArray);
            img.scaleAbsolute(150,150);

        }catch (Exception e) {
            Log.e("bitmapToImage", "error: " + e.getMessage(), e);
        }
        return img;
    }


    void openPdf() {
        ImageView iv = (ImageView) findViewById(R.id.imageView);

        try {
            ParcelFileDescriptor fb = ParcelFileDescriptor.open(new File("/sdcard/test.pdf"), ParcelFileDescriptor.MODE_READ_ONLY);
            PdfiumCore pdfiumCore = new PdfiumCore(this);
        }catch (Exception e) {
            Log.e("openPdf", "error: " + e.getMessage(), e);
        }

//        ParcelFileDescriptor fd = ...;
        int pageNum = 0;
        PdfiumCore pdfiumCore = new PdfiumCore(this);
        try {
            com.shockwave.pdfium.PdfDocument pdfDocument = pdfiumCore.newDocument(fd);

            pdfiumCore.openPage(pdfDocument, pageNum);

            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNum);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNum);

            // ARGB_8888 - best quality, high memory usage, higher possibility of OutOfMemoryError
            // RGB_565 - little worse quality, twice less memory usage
            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.RGB_565);
            pdfiumCore.renderPageBitmap(pdfDocument, bitmap, pageNum, 0, 0,
                    width, height);
            //if you need to render annotations and form fields, you can use
            //the same method above adding 'true' as last param

            iv.setImageBitmap(bitmap);

            printInfo(pdfiumCore, pdfDocument);

            pdfiumCore.closeDocument(pdfDocument); // important!
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }
*/
    public void printInfo(PdfiumCore core, PdfDocument doc) {
        PdfDocument.Meta meta = core.getDocumentMeta(doc);
        Log.e(null, "title = " + meta.getTitle());
        Log.e(null, "author = " + meta.getAuthor());
        Log.e(null, "subject = " + meta.getSubject());
        Log.e(null, "keywords = " + meta.getKeywords());
        Log.e(null, "creator = " + meta.getCreator());
        Log.e(null, "producer = " + meta.getProducer());
        Log.e(null, "creationDate = " + meta.getCreationDate());
        Log.e(null, "modDate = " + meta.getModDate());

        printBookmarksTree(core.getTableOfContents(doc), "-");

    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(null, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

}