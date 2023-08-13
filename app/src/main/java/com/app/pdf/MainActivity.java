package com.app.pdf;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
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
            File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "prueba.pdf");
            PdfWriter.getInstance(documento, new FileOutputStream(pdfFile));
            documento.open();


            documento.add(createTableImage(30, R.mipmap.ic_google_foreground, 0, 0));
            documento.add(createTable(FONT_TITLE, "LS24R350FZLXZX", 0, 50));
            documento.add(createTable(FONT_FOOTER, "LS24R350FZLXZX", 10, 0));
            documento.add(createTable(FONT_FOOTER, "B09PRSV4MR", 10, 10));
            documento.add(createTableImage(55, R.mipmap.ic_qr_foreground, 0, 0));
            documento.add(createTable(FONT_FOOTER, "QR", 10, 0));
            documento.add(createTable(FONT_FOOTER, "https://www.amazon.com.mx/SAMSUNG-LS24R350FZLXZX-Monitor-Experiencia-inmersiva/dp/B09PRSV4MR?ref_=Oct_DLandingS_D_7c6e4843_0", 10, 0));

            documento.close();

            Toast.makeText(this, "GG", Toast.LENGTH_SHORT).show();

        }catch(Exception e) {
            Log.e("createPDF", "error: " + e.getMessage(), e);
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

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
}