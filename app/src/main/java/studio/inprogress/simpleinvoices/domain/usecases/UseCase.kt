package studio.inprogress.simpleinvoices.domain.usecases

interface UseCase<out Result, in Params> where Result : Any?, Params : UseCase.Parameters {
    @Suppress("UNCHECKED_CAST")
    suspend operator fun invoke(param: Params = Empty() as Params): Result

    interface Parameters
    class Empty : Parameters
}
