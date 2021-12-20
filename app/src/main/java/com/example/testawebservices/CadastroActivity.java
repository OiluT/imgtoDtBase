package com.example.testawebservices;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLTransactionRollbackException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

import com.github.rtoshiro.util.format.MaskFormatter;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.example.testawebservices.MoneyTextWatcher;

public class CadastroActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


	String textoTipoNota;
	Bitmap bitmap;
	private ImageView ivSelectedImage;
	private static final int GALERIA_REFEICAO = 1;
	private static final int PERMISSAO_REQUEST = 2;
	private static final int TIRAR_FOTO = 3;


	EditText edData;
	EditText edDataMysql;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cadastro);

		edData = findViewById(R.id.tvDate);
		edData.setInputType(InputType.TYPE_NULL);


		edData.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showDateTimeDialog(edData);
			}
		});





//		edData.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				Calendar cal = Calendar.getInstance();
//				int year = cal.get(Calendar.YEAR);
//				int month = cal.get(Calendar.MONTH);
//				int day = cal.get(Calendar.DAY_OF_MONTH);
//				int hour = cal.get(Calendar.HOUR_OF_DAY);
//				int minute = cal.get(Calendar.MINUTE);
//
//				DatePickerDialog dialog = new DatePickerDialog(
//						CadastroActivity.this,
//						android.R.style.Theme_Holo_Light_Dialog_MinWidth,
//						mDateSetListener,
//						year,month,day);
//				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//				dialog.show();
//			}
//		});
//
//		mDateSetListener = new DatePickerDialog.OnDateSetListener() {
//			@Override
//			public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//				month = month + 1;
//				Log.d(TAG, "onDateSet: dd/mm/yyyy: " + day + "/" + month + "/" + year);
//
//				String date = day + "/" + month + "/" + year;
//				edData.setText(date);
//			}
//		};




		if (android.os.Build.VERSION.SDK_INT > 7)
		{
			StrictMode.ThreadPolicy policy = new
					StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED){

			if (ActivityCompat.shouldShowRequestPermissionRationale(this,
					Manifest.permission.READ_EXTERNAL_STORAGE)){

			} else {
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
						PERMISSAO_REQUEST);
			}
		}

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED){

			if (ActivityCompat.shouldShowRequestPermissionRationale(this,
					Manifest.permission.WRITE_EXTERNAL_STORAGE)){

			} else {
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
						PERMISSAO_REQUEST);
			}
		}


		final TextView edNome = (TextView) findViewById(R.id.etNome);
		final EditText edValorNota = (EditText) findViewById(R.id.edValorNota);



		Button btCadastro = (Button) findViewById(R.id.btCadastro);
		Button btSelect = (Button) findViewById(R.id.btSelect);
		Button btTirarFoto = (Button) findViewById(R.id.btSelectFoto);
		Button btSalvarPDF = (Button) findViewById(R.id.salvarPDF);
		Button btEnviaPDF = (Button) findViewById(R.id.btEnviaPDF);

		edValorNota.addTextChangedListener(new MoneyTextWatcher(edValorNota));


		Spinner spinner = findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
				(this, R.array.tipoNota, android.R.layout.simple_list_item_1);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);



//####################### CRIANDO MÁSCARA 0000,00 #########################################
	//	SimpleMaskFormatter smf = new SimpleMaskFormatter("N.NNN,NN");
	//	MaskTextWatcher mtw = new MaskTextWatcher(edValorNota, smf);
	//	edValorNota.addTextChangedListener(mtw);
//################################################################################

		ivSelectedImage = (ImageView) findViewById(R.id.ivSelectedImage);

		Intent intent = getIntent();
		if (intent != null){
		    Bundle params = intent.getExtras();
		    if (params != null) {
		        String pasta = params.getString("paste");
		        edNome.setText("" +pasta);
            }
        }


//################################# CRIA ARQUIVO PDF PARA ENVIO SE DEUS QUISER ####################
		btEnviaPDF.setOnClickListener(new OnClickListener() {

			public String validar() {
				String texto_erros = "";
				if (bitmap == null) {
					texto_erros = "A FOTO é obrigatória\n";
				}
				return texto_erros;
			}


			@RequiresApi(api = Build.VERSION_CODES.KITKAT)
			@Override
			public void onClick(View v) {
				String erros = validar();
				if (erros.equals("")) {
					PdfDocument documentoPDF = new PdfDocument();

					PdfDocument.PageInfo detalhesDaPagina =
							new PdfDocument.PageInfo.Builder(500, 55, 1).create();
					//CRIA A PRIMEIRA PÁGINA
					PdfDocument.Page novaPagina = documentoPDF.startPage(detalhesDaPagina);

					Canvas canvas = novaPagina.getCanvas();
					// INICIA OBJETO SELEÇÃO DE COR
					Paint corDoTexto = new Paint();
					Paint corDoTexto2 = new Paint();
					// SELECIONA A COR
					corDoTexto.setColor(Color.BLACK);
					corDoTexto2.setColor(Color.GREEN);
					// ADICIONA OS TEXTOS DIGITADOS AO PDF
					canvas.drawText(edNome.getText().toString(), 5, 13, corDoTexto);
					canvas.drawText(edData.getText().toString(), 5, 30, corDoTexto);
					canvas.drawText("R$",5,45, corDoTexto2);
					canvas.drawText(edValorNota.getText().toString(),20,45, corDoTexto);
					canvas.drawText("Gasto em:",75,45,corDoTexto);
					canvas.drawText(textoTipoNota, 140, 45, corDoTexto);


					// FINALIZA A PRIMEIRA PÁGINA
					documentoPDF.finishPage(novaPagina);

					// CRIA SEGUNDA PÁGINA
					detalhesDaPagina = new PdfDocument.PageInfo.Builder(500, 500, 2).create();
					novaPagina = documentoPDF.startPage(detalhesDaPagina);

					canvas = novaPagina.getCanvas();
					corDoTexto = new Paint();

					// CRIA PARAMETRO PARA RECEBER AS ESCALAS DA IMAGEM SELECIONADA
					bitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, true);

					corDoTexto.setColor(Color.BLUE);
					// INICIALIZA A IMAGEM PARA O PDF
					canvas.drawBitmap(bitmap, 0, 0, null);

					documentoPDF.finishPage(novaPagina);

					Usuario Nome = new Usuario();
					Nome.setNome(edNome.getText().toString());
					// CRIA O PDF NO SDCARD
					String targetPdf = "/sdcard/" + Nome + ".pdf";
					File filePath = new File(targetPdf);


					try {
						documentoPDF.writeTo(new FileOutputStream(filePath));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					documentoPDF.close();
//##################################################################################################
//################################ ENVIA AS INFORMAÇÕES PARA O SERVIDOR ############################
					File file = new File(String.valueOf(filePath));
					byte[] bFile = new byte[(int) file.length()];
					FileInputStream fileInputStream = null;
					try {
						fileInputStream = new FileInputStream(file);
						fileInputStream.read(bFile);
						fileInputStream.close();
						System.out.println("RESULTADOOOOOOOOOOOOOOO" + file);
						System.out.println("Meu Array----------" + Arrays.toString(bFile));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println(e);
					}

					UsuarioDAO dao = new UsuarioDAO();
					Usuario usr = new Usuario();

					usr.setNome(edNome.getText().toString());
					usr.setData(edData.getText().toString());
					usr.setFoto(bFile);
					usr.setTipoNota(textoTipoNota);

					String valorConvertido = MoneyTextWatcher.formatPriceSave(edValorNota.getText().toString());
					System.out.println(valorConvertido);

					usr.setValorNota(valorConvertido);

					usr = dao.inserirUsuario(usr);

					UsuarioDAO PDF = new UsuarioDAO();
					boolean result = dao.salvarDiretorio(null);
					boolean resultado = dao.salvarPDF(null);


					if (usr != null && resultado && result) {
						Toast.makeText(CadastroActivity.this, "Enviado com Sucesso!", Toast.LENGTH_LONG).show();
						finish();
					}
				}else {
					Toast.makeText(CadastroActivity.this, "A Imagem é Obrigatória", Toast.LENGTH_LONG).show();
				}
			}
		});
//##################################################################################################

		btSalvarPDF.setOnClickListener(new OnClickListener() {
			@RequiresApi(api = Build.VERSION_CODES.KITKAT)
			@Override
			public void onClick(View v) {


				PdfDocument documentoPDF = new PdfDocument();

				PdfDocument.PageInfo detalhesDaPagina =
						new PdfDocument.PageInfo.Builder(500, 500, 1).create();
				//CRIA A PRIMEIRA PÁGINA
				PdfDocument.Page novaPagina = documentoPDF.startPage(detalhesDaPagina);

				Canvas canvas = novaPagina.getCanvas();
				// INICIA OBJETO SELEÇÃO DE COR
				Paint corDoTexto = new Paint();
				// SELECIONA A COR
				corDoTexto.setColor(Color.BLACK);
				// ADICIONA OS TEXTOS DIGITADOS AO PDF
				canvas.drawText(edNome.getText().toString(),105, 100, corDoTexto);
				canvas.drawText(edData.getText().toString(), 105,125, corDoTexto);


				// FINALIZA A PRIMEIRA PÁGINA
				documentoPDF.finishPage(novaPagina);

				// CRIA SEGUNDA PÁGINA
				detalhesDaPagina = new PdfDocument.PageInfo.Builder(500,500,2).create();
				novaPagina = documentoPDF.startPage(detalhesDaPagina);

				canvas = novaPagina.getCanvas();
				corDoTexto = new Paint();

				// CRIA PARAMETRO PARA RECEBER AS ESCALAS DA IMAGEM SELECIONADA
				bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

				corDoTexto.setColor(Color.BLUE);
				// INICIALIZA A IMAGEM PARA O PDF
				canvas.drawBitmap(bitmap,0,0,null);

				documentoPDF.finishPage(novaPagina);

				Usuario Nome = new Usuario();
				Nome.setNome(edNome.getText().toString());
				// CRIA O PDF NO SDCARD
				String targetPdf = "/sdcard/pdf/"+Nome+".pdf";
				File filePath = new File(targetPdf);

				try {
					documentoPDF.writeTo(new FileOutputStream(filePath));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				documentoPDF.close();
			}
		});

		btSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK,
						MediaStore.Images.Media.INTERNAL_CONTENT_URI);
				startActivityForResult(Intent.createChooser(intent, "selecione uma imagem"), GALERIA_REFEICAO);
			}
		});
		btTirarFoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
					startActivityForResult(takePictureIntent, TIRAR_FOTO);
				}

			}
		});

		btCadastro.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Bitmap bitmap = ((BitmapDrawable) ivSelectedImage.getDrawable()).getBitmap();

				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] byteArray = stream.toByteArray();

				UsuarioDAO dao = new UsuarioDAO();
				Usuario usr = new Usuario();

				usr.setNome(edNome.getText().toString());
				usr.setData(edData.getText().toString());
				usr.setFoto(byteArray);

				usr = dao.inserirUsuario(usr);

				if (usr != null) {
					finish();
				} else {
					Toast.makeText(CadastroActivity.this, "Erro no Cadastro", Toast.LENGTH_LONG).show();
				}
			}
		});

	}

	private void showDateTimeDialog(EditText edData) {

		final Calendar calendar=Calendar.getInstance();
		DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
				calendar.set(Calendar.YEAR,year);
				calendar.set(Calendar.MONTH,month);
				calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

				TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
						calendar.set(Calendar.MINUTE,minute);

						SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm");

						edData.setText(simpleDateFormat.format(calendar.getTime()));

					}
				};

				new TimePickerDialog(CadastroActivity.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
			}
		};

		new DatePickerDialog(CadastroActivity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != Activity.RESULT_CANCELED) {
			if (requestCode == GALERIA_REFEICAO) {
				Uri selectedImage = data.getData();

				String[] filePath = {MediaStore.Images.Media.DATA};

				Cursor cursor = getContentResolver().query(
						selectedImage, filePath, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePath[0]);

				String picturePath = cursor.getString(columnIndex);

				cursor.close();

				bitmap = BitmapFactory.decodeFile(picturePath);
				ivSelectedImage.setImageBitmap(bitmap);

			}
		}
		if (requestCode == TIRAR_FOTO && resultCode == RESULT_OK) {

			Bundle extras = data.getExtras();
			bitmap = (Bitmap) extras.get("data");
			ivSelectedImage.setImageBitmap(bitmap);

		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		textoTipoNota = parent.getItemAtPosition(position).toString();
		Toast.makeText(parent.getContext(), textoTipoNota, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}


}
