package com.mycompany.mavenproject3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SalesForm extends JFrame {
    private JComboBox<String> productComboBox;
    private JTextField priceField;
    private JTextField stockField;
    private JTextField qtyField;
    private JButton processButton;
    private List<Product> products;
    private ProductForm productForm;

    public SalesForm(ProductForm productForm) {
        this.productForm = productForm;
        this.products = productForm.getProducts();

        setTitle("WK. Cuan | Form Penjualan");
        setSize(500, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 240, 240)); // Light gray background

        // Panel utama
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        formPanel.setBackground(new Color(255, 255, 255)); // White background

        // Set font for labels and fields
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        JLabel productLabel = new JLabel("Pilih Produk:");
        productLabel.setFont(labelFont);
        formPanel.add(productLabel);

        productComboBox = new JComboBox<>();
        for (Product product : products) {
            productComboBox.addItem(product.getName());
        }
        formPanel.add(productComboBox);

        JLabel priceLabel = new JLabel("Harga Jual:");
        priceLabel.setFont(labelFont);
        formPanel.add(priceLabel);
        priceField = new JTextField();
        priceField.setEditable(false);
        priceField.setFont(fieldFont);
        formPanel.add(priceField);

        JLabel stockLabel = new JLabel("Stok Tersedia:");
        stockLabel.setFont(labelFont);
        formPanel.add(stockLabel);
        stockField = new JTextField();
        stockField.setEditable(false);
        stockField.setFont(fieldFont);
        formPanel.add(stockField);

        JLabel qtyLabel = new JLabel("Qty:");
        qtyLabel.setFont(labelFont);
        formPanel.add(qtyLabel);
        qtyField = new JTextField();
        qtyField.setFont(fieldFont);
        formPanel.add(qtyField);

        processButton = new JButton("Proses");
        processButton.setFont(new Font("Arial", Font.BOLD, 14));
        processButton.setBackground(new Color(0, 123, 255)); // Blue background
        processButton.setForeground(Color.WHITE); // White text
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 255, 255)); // White background
        buttonPanel.add(processButton);

        // Tambahkan panel ke JFrame
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Tampilkan detail produk saat dipilih
        productComboBox.addActionListener(e -> updateProductInfo());

        // Proses penjualan
        processButton.addActionListener(e -> processSale());

        updateProductInfo(); // tampilkan produk pertama saat form dibuka
    }

    private void updateProductInfo() {
        int index = productComboBox.getSelectedIndex();
        if (index != -1) {
            Product selected = products.get(index);
            priceField.setText(String.valueOf(selected.getPrice()));
            stockField.setText(String.valueOf(selected.getStock()));
        }
    }

    private void processSale() {
        int index = productComboBox.getSelectedIndex();
        if (index == -1) return;

        try {
            int qty = Integer.parseInt(qtyField.getText());
            Product selected = products.get(index);

            if (qty > selected.getStock()) {
                JOptionPane.showMessageDialog(this, "Stok tidak cukup!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double total = selected.getPrice() * qty;
            selected.setStock(selected.getStock() - qty);

            if (selected.getStock() == 0) {
                products.remove(index);
            }

            productForm.loadProductData(products);
            productForm.repaint();

            JOptionPane.showMessageDialog(this,
                "Penjualan berhasil!\n" +
                "Produk: " + selected.getName() + "\n" +
                "Jumlah: " + qty + "\n" +
                "Total: Rp " + total);

            productComboBox.removeAllItems();
            for (Product p : products) {
                productComboBox.addItem(p.getName());
            }

            updateProductInfo(); // update stok di tampilan
            qtyField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Qty tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateProductList() {
        products = productForm.getProducts(); // Ambil ulang daftar produk terbaru
        productComboBox.removeAllItems();
        for (Product product : products) {
            if (product.getStock() > 0) {
                productComboBox.addItem(product.getName());
            }
        }
        updateProductInfo();
    }
}
