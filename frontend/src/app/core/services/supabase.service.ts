import { Injectable } from '@angular/core';
import { createClient, SupabaseClient } from '@supabase/supabase-js';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SupabaseService {
  private readonly supabase: SupabaseClient;

  constructor() {
    this.supabase = createClient(
      environment.supabaseUrl,
      environment.supabaseAnonKey
    );
  }

  signIn(email: string, password: string) {
    return this.supabase.auth.signInWithPassword({
      email,
      password
    });
  }

  signUp(email: string, password: string) {
    return this.supabase.auth.signUp({
      email,
      password
    });
  }

  signOut() {
    return this.supabase.auth.signOut();
  }

  async getSession() {
    const { data } = await this.supabase.auth.getSession();
    return data.session;
  }

  get client() {
    return this.supabase;
  }
}
