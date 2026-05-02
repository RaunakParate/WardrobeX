package com.example.wardrobex.ui.wardrobe;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0006J\b\u0010\u0012\u001a\u00020\u0010H\u0002R\u001a\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/example/wardrobex/ui/wardrobe/WardrobeViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "_allClothing", "Landroidx/lifecycle/MutableLiveData;", "", "Lcom/example/wardrobex/model/Clothing;", "allClothing", "Landroidx/lifecycle/LiveData;", "getAllClothing", "()Landroidx/lifecycle/LiveData;", "db", "Lcom/google/firebase/firestore/FirebaseFirestore;", "wardrobeCollection", "Lcom/google/firebase/firestore/CollectionReference;", "delete", "", "clothing", "fetchClothing", "app_debug"})
public final class WardrobeViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.firestore.FirebaseFirestore db = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.firestore.CollectionReference wardrobeCollection = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.util.List<com.example.wardrobex.model.Clothing>> _allClothing = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<com.example.wardrobex.model.Clothing>> allClothing = null;
    
    public WardrobeViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.example.wardrobex.model.Clothing>> getAllClothing() {
        return null;
    }
    
    private final void fetchClothing() {
    }
    
    public final void delete(@org.jetbrains.annotations.NotNull()
    com.example.wardrobex.model.Clothing clothing) {
    }
}