import { Injectable } from '@angular/core'

@Injectable({ providedIn: 'root' })
export class SupabaseE2eService {
  async getSession() {
    return {
      user: {
        id: 'c004a123-226d-4c19-b44e-5b7251f09282',
        email: 'e2e@careerpath.dev',
      },
    }
  }

  async signOut() {
    return
  }
}
