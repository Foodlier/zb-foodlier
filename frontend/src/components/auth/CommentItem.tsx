import { useNavigate } from 'react-router-dom'
import * as S from '../../styles/auth/CommentItem.styled'
import { Comment } from '../../constants/Interfaces'

interface CommentItemProps {
  item: Comment
}

const CommentItem = ({ item }: CommentItemProps) => {
  const navigate = useNavigate()
  return (
    <S.Container onClick={() => navigate(`/recipe/detail/${item.recipeId}`)}>
      <S.Text>{item.message}</S.Text>
      <S.Text>{item.createdAt}</S.Text>
    </S.Container>
  )
}

export default CommentItem
